package api.com.medhead.localize.service;

import api.com.medhead.localize.model.Hospital;
import api.com.medhead.localize.model.Location;
import api.com.medhead.localize.payload.request.PatientSearchRequest;

import api.com.medhead.localize.utils.Utils;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.PointList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


@Service
public class GraphhopperService {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    HospitalService hospitalService;

    public List<Hospital> getNearestHospital(PatientSearchRequest patientSearchRequest, int locationSearchPerimeterMeters){
        GraphHopper hopper = createGraphHopperInstance("ms-localize/src/main/resources/england-latest.osm.pbf");
        List<Hospital> hospitals = findHospitalsWithinPerimeter(locationSearchPerimeterMeters, patientSearchRequest.getLatitude(), patientSearchRequest.getLongitude(), patientSearchRequest.getSpecialityId());
        List<Hospital> nearestHospitals = routing(hopper, patientSearchRequest.getLatitude(), patientSearchRequest.getLongitude(), hospitals);
        // release resources to properly shutdown or start a new instance
        hopper.close();
        return nearestHospitals;
    }

    static GraphHopper createGraphHopperInstance(String ghLoc) {
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc);
        // specify where to store graphhopper files
        hopper.setGraphHopperLocation("target/routing-graph-cache");

        // see docs/core/profiles.md to learn more about profiles
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        // this enables speed mode for the profile we called car
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        // now this can take minutes if it imports or a few seconds for loading of course this is dependent on the area you import
        hopper.importOrLoad();
        return hopper;
    }

    public List<Hospital> findHospitalsWithinPerimeter(int meters, Double latitude, Double longitude, int specialityId){
        Location myLoc = Utils.generateLocation(meters, latitude, longitude);
        return hospitalService.findHospitalWithinPerimeter(myLoc.getLatitudeLeft(), myLoc.getLatitudeRight(), myLoc.getLongitudeRight(), myLoc.getLongitudeLeft(), specialityId);
    }

    public List<Hospital> routing(GraphHopper hopper, Double latitude, Double longitude, List<Hospital> hospitals) {
       List<Hospital> hospitalListSortedByTime = new ArrayList<>();

        for (int i = 0; i <hospitals.size();++i) {

            // simple configuration of the request object
            GHRequest req = new GHRequest(latitude, longitude, hospitals.get(i).getLatitude(), hospitals.get(i).getLongitude()).
                    // note that we have to specify which profile we are using even when there is only one like here
                            setProfile("car").
                    // define the language for the turn instructions
                            setLocale(Locale.UK);
            GHResponse rsp = hopper.route(req);

            // handle errors
            if (rsp.hasErrors())
                System.out.println(rsp.getErrors().toString());

            // use the best path, see the GHResponse class for more possibilities.
            ResponsePath path = rsp.getBest();

            // points, distance in meters and time in millis of the full path
            PointList pointList = path.getPoints();
            double distance = path.getDistance();
            long timeInMs = path.getTime()/500;

            hospitals.get(i).setDistanceInTime(timeInMs);
            hospitals.get(i).setFormattedDistanceInKm(df.format(Double.valueOf(distance) / 1000) + " km");
            hospitals.get(i).setFormattedDistanceInTime(Utils.convertSecondsToTime(timeInMs));
            hospitalListSortedByTime.add(hospitals.get(i));
        }
        Collections.sort(hospitalListSortedByTime, Hospital.HospitalTimeComparator);
        return  hospitalListSortedByTime;
    }
}
