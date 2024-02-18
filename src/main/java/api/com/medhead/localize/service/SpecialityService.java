package api.com.medhead.localize.service;

import api.com.medhead.localize.model.Speciality;
import api.com.medhead.localize.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;

    public Speciality findOneById(int id){
       return  specialityRepository.findOneById(id);
    }
    public Speciality getSpecialityByName(String name){
        return specialityRepository.findByName(name);
    }
    public Speciality save(Speciality speciality){
       return  specialityRepository.save(speciality);
    }
    public List<Speciality> getSpecialities() {
        return specialityRepository.findAll();
    }
    public List<Speciality> getSpecialitiesBySpecialityGroupId(int groupId) {
        return specialityRepository.findBySpecialityGroupId(groupId);
    }
    public List<Speciality> getSpecialitiesBySpecialityGroupName(String name) {
        return specialityRepository.findBySpecialityGroupName(name);
    }


    public Speciality addSpeciality(Speciality speciality) {
        return specialityRepository.save(speciality);
    }

}

