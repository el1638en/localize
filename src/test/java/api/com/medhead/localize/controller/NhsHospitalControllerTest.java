package api.com.medhead.localize.controller;


import api.com.medhead.localize.model.Hospital;
import api.com.medhead.localize.model.Speciality;
import api.com.medhead.localize.model.SpecialityGroup;
import api.com.medhead.localize.payload.request.PatientSearchRequest;
import api.com.medhead.localize.payload.request.SpecialityGroupRequest;
import api.com.medhead.localize.repository.HospitalRepository;
import api.com.medhead.localize.repository.SpecialityGroupRepository;
import api.com.medhead.localize.repository.SpecialityRepository;
import api.com.medhead.localize.service.SpecialityGroupService;
import api.com.medhead.localize.service.SpecialityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NhsHospitalControllerTest {


    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres:15-alpine");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);

    }

    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private SpecialityGroupService specialityGroupService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    //    @Mock
//    HospitalService hospitalService;
//    @Mock
//    GraphhopperService graphhopperService;
//    @MockBean
    @Autowired
    private HospitalRepository hospitalRepository;
    //    @MockBean
    @Autowired
    private SpecialityRepository specialityRepository;
    //    @MockBean
    @Autowired
    private SpecialityGroupRepository specialityGroupRepository;

    private Hospital hospital0 = new Hospital(224, "Bedford Hospital South Wing", "South Wing Kempston Road", "Bedford", "MK42 9DJ", -0.47248, 52.12826, "1234355122", "http://www.bedfordhospital.nhs.uk/index.asp", "pals@bedfordhospital.nhs.uk", 1);
    private Hospital hospital1 = new Hospital(539, "Calderdale Royal Hospital", "Salterhebble", "Halifax", "HX3 0PW", -1.85749, 53.70482, "01422 357171", "http://www.cht.nhs.uk", "", 1);
    private Hospital hospital2 = new Hospital(227, "St Monicas Hospital", "Long Street Easingwold", "York", "YO61 3JD", -1.18931, 54.1184, "01904 724825", "https://www.yorkhospitals.nhs.uk/our-hospitals/st-monicas-easingwold/", "", 2);
    private Hospital hospital3 = new Hospital(1154, "Bicester Community Hospital", "Piggy Lane", "Bicester", "OX26 6HT", -1.15806, 51.89619, "", "", "", 4);
    private List<Hospital> hospitals = new ArrayList<>();
    private Speciality speciality0 = new Speciality(12, "Anesthésie");
    private Speciality speciality1 = new Speciality(16, "Soins Intensifs");
    private Speciality speciality2 = new Speciality(17, "Service de santé communautaire dentaire");
    private Speciality speciality3 = new Speciality(19, "Santé publique dentaire");
    private Speciality speciality4 = new Speciality(2, "Service de santé communautaire médical");
    private List<Speciality> specialities0 = new ArrayList<>();
    private List<Speciality> specialities1 = new ArrayList<>();
    private List<Speciality> specialities2 = new ArrayList<>();
    private List<Speciality> specialities3 = new ArrayList<>();
    private List<Speciality> specialities = new ArrayList<>();
    private List<Speciality> specialitiesWithGroup1 = new ArrayList<>();

    private SpecialityGroup specialityGroup0 = new SpecialityGroup(4, "Groupe dentaire");
    private SpecialityGroup specialityGroup1 = new SpecialityGroup(9, "Anesthésie");
    private List<SpecialityGroup> specialityGroups = new ArrayList<>();
    private SpecialityGroupRequest specialityGroupRequest = new SpecialityGroupRequest();

    private List<Hospital> hospitalsFound = new ArrayList<>();
    private PatientSearchRequest patientSearchRequest = new PatientSearchRequest();

    @Value("${location.search.perimeter.meters}")
    private int locationSearchPerimeters;

    @BeforeEach
    public void setup_Test() {
        specialities.add(speciality0);
        specialities.add(speciality1);
        specialities.add(speciality2);
        specialities.add(speciality3);
        specialities.add(speciality4);

        speciality0.setSpecialityGroup(specialityGroup0);
        speciality1.setSpecialityGroup(specialityGroup0);
        speciality2.setSpecialityGroup(specialityGroup1);
        speciality3.setSpecialityGroup(specialityGroup1);
        speciality4.setSpecialityGroup(specialityGroup1);

        specialities0.add(speciality1);
        specialities0.add(speciality2);
        specialities1.add(speciality0);
        specialities1.add(speciality4);
        specialities2.add(speciality3);
        specialities2.add(speciality1);
        specialities3.add(speciality2);
        specialities3.add(speciality0);
        specialities3.add(speciality3);

        specialitiesWithGroup1.add(speciality0);
        specialitiesWithGroup1.add(speciality1);

        hospital0.setSpecialities(specialities0);
        hospital1.setSpecialities(specialities1);
        hospital2.setSpecialities(specialities2);
        hospital3.setSpecialities(specialities3);

        hospitals.add(hospital0);
        hospitals.add(hospital1);
        hospitals.add(hospital2);
        hospitals.add(hospital3);

        specialityGroupRequest.setName(specialityGroup1.getName());
        specialityGroupRequest.setId(9);

        specialityGroups.add(specialityGroup0);
        specialityGroups.add(specialityGroup1);

        patientSearchRequest.setLatitude(51.488954);
        patientSearchRequest.setLongitude(-0.163229);
        patientSearchRequest.setSpecialityId(16);
        hospitalsFound.add(hospital0);
        hospitalsFound.add(hospital2);
    }

    @Test
    public void getAllHospitals() throws Exception {
//        hospitalRepository.saveAll(hospitals);
//        when(hospitalRepository.findAll()).thenReturn(hospitals);
//        List<Hospital> hospitals = NhsHospitalController.getAllHospitals();
//        assertEquals(4, hospitals.size());
//        assertEquals("Calderdale Royal Hospital", hospitals.get(1).getName());

        mockMvc.perform(MockMvcRequestBuilders
                .get(NhsHospitalController.PATH+"/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // THEN
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    public void getAllSpecialities() {
//        when(specialityRepository.findAll()).thenReturn(specialities);
//        List<Speciality> specialities = nhsHospitalController.getAllSpecialities();
//        assertEquals(5, specialities.size());
//        assertEquals("Anesthésie", specialities.get(0).getName());
//    }
//
//    @Test
//    public void getSpecialitiesBySpecialityGroupName() {
//        when(specialityRepository.findBySpecialityGroupId(specialityGroupRequest.getId())).thenReturn(specialitiesWithGroup1);
//        List<Speciality> specialities = nhsHospitalController.getSpecialitiesBySpecialityGroup(specialityGroupRequest);
//        assertEquals(2, specialities.size());
//        assertEquals("Anesthésie", specialities.get(0).getName());
//    }
//
//    @Test
//    public void getAllSpecialityGroups() {
//        when(specialityGroupRepository.findAll()).thenReturn(specialityGroups);
//        List<SpecialityGroup> specialityGroups = nhsHospitalController.getAllSpecialityGroups();
//        assertEquals(2, specialityGroups.size());
//        assertEquals("Anesthésie", specialityGroups.get(1).getName());
//    }
//
//    @Test
//    public void getPerimeter() {
//        int searchPerimeter = nhsHospitalController.getPerimeter();
//        assertEquals(9000, searchPerimeter);
//    }
//
//    @Test
//    public void getNearestHospital() {
//        Location loc = generateLocation(locationSearchPerimeters, patientSearchRequest.getLatitude(), patientSearchRequest.getLongitude());
//        when(hospitalRepository.findHospitalWithinPerimeterWithSpeciality(loc.getLatitudeLeft(), loc.getLatitudeRight(), loc.getLongitudeRight(), loc.getLongitudeLeft(), 16)).thenReturn(hospitalsFound);
//        List<Hospital> hospitals = nhsHospitalController.getNearestHospital(patientSearchRequest);
//        assertEquals(2, hospitals.size());
//    }
}
