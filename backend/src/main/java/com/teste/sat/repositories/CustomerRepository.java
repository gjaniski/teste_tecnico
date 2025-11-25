package com.teste.sat.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teste.sat.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Query("SELECT c.id FROM Customer c WHERE c.isActive = true")
    Page<Long> findActiveIds(Pageable pageable);

    long countByIsActiveTrue();

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.status WHERE c.id IN :ids")
    List<Customer> findCustomersWithStatus(@Param("ids") List<Long> ids);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.status WHERE c.id = :id")
    Optional<Customer> findById(Long id);

    Optional<Customer> findByEmailAndIsActiveTrue(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.status s WHERE s.id = :statusId AND c.isActive = true")
    Page<Customer> findCustomersByStatusId(@Param("statusId") Long statusId, Pageable pageable);

}