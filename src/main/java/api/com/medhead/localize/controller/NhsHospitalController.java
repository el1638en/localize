package api.com.medhead.localize.controller;


import api.com.medhead.localize.model.Hospital;
import api.com.medhead.localize.model.Speciality;
import api.com.medhead.localize.model.SpecialityGroup;
import api.com.medhead.localize.payload.request.PatientSearchRequest;
import api.com.medhead.localize.payload.request.SpecialityGroupRequest;
import api.com.medhead.localize.service.GraphhopperService;
import api.com.medhead.localize.service.HospitalService;
import api.com.medhead.localize.service.SpecialityGroupService;
import api.com.medhead.localize.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(NhsHospitalController.PATH)
public class NhsHospitalController {

    public static final String PATH = "/api/hospitals";

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private GraphhopperService graphhopperService;
    @Autowired
    private SpecialityGroupService specialityGroupService;

    @Value("${location.search.perimeter.meters}")
    private int locationSearchPerimeterMeters;

    @GetMapping("all")
    public List<Hospital> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("specialities")
    public List<Speciality> getAllSpecialities() {
        return specialityService.getSpecialities();
    }

    @PostMapping("specialities")
    public List<Speciality> getSpecialitiesBySpecialityGroup(@Validated @RequestBody SpecialityGroupRequest specialityGroupRequest) {
        return specialityService.getSpecialitiesBySpecialityGroupId(specialityGroupRequest.getId());
    }

    @GetMapping("speciality-groups")
    public List<SpecialityGroup> getAllSpecialityGroups() {
        return specialityGroupService.getAllSpecialityGroups();
    }

    @GetMapping("perimeter")
    public int getPerimeter(){
        return locationSearchPerimeterMeters;
    }

    @PostMapping("getNearest")
    public List<Hospital> getNearestHospital(@Validated @RequestBody PatientSearchRequest patientSearchRequest){
         return graphhopperService.getNearestHospital(patientSearchRequest, getPerimeter());
    }



}
