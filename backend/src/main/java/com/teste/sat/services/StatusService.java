package com.teste.sat.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.models.Status;
import com.teste.sat.repositories.StatusRepository;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;
    
    public List<StatusDTO> findAll() {
        List<Status> statuses = statusRepository.findAll();
        
        return statuses.stream()
            .map(st -> new StatusDTO(st.getId(), st.getName()))
            .collect(Collectors.toList());
    }    
    
    public List<Status> findAllByIdIn(List<Long> idList) {
        List<Status> status = statusRepository.findAllByIdIn(idList);
        
        return status.stream()
            .collect(Collectors.toList());
    }
        
}
