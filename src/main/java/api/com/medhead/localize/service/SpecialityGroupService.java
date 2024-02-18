package api.com.medhead.localize.service;

import api.com.medhead.localize.model.SpecialityGroup;
import api.com.medhead.localize.repository.SpecialityGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityGroupService {

    @Autowired
    private SpecialityGroupRepository specialityGroupRepository;

    public SpecialityGroup findByName(String name){
       return  specialityGroupRepository.findByName(name);
    }
    public List<SpecialityGroup> getAllSpecialityGroups() {
        return specialityGroupRepository.findAll();
    }
}

