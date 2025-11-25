package com.teste.sat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.sat.models.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    
    List<Status> findAllByIdIn(List<Long> customerId);

}
