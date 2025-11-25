package com.teste.sat.repositories;

import com.teste.sat.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testFindByEmailAndIsActiveTrue() {
        Optional<Customer> customer = customerRepository.findByEmailAndIsActiveTrue("joao.pedrosa@teste.com");
        assertTrue(customer.isPresent());
        assertEquals("João Pedrosa", customer.get().getName());
    }

    @Test
    void testFindActiveIds() {
        Page<Long> idsPage = customerRepository.findActiveIds(PageRequest.of(0, 10));
        assertEquals(5, idsPage.getTotalElements());
        assertTrue(idsPage.getContent().contains(1L));
        assertTrue(idsPage.getContent().contains(5L));
    }

    @Test
    void testFindCustomersWithStatus() {
        List<Customer> customers = customerRepository.findCustomersWithStatus(List.of(1L, 2L));
        assertEquals(2, customers.size());

        Customer joao = customers.stream().filter(c -> c.getId() == 1L).findFirst().orElse(null);
        assertNotNull(joao);
        assertEquals(2, joao.getStatus().size());
    }

    @Test
    void testFindById() {
        Optional<Customer> customer = customerRepository.findById(2L);
        assertTrue(customer.isPresent());
        assertEquals("Maria Silva", customer.get().getName());
    }

    @Test
    void testFindCustomersByStatusId() {
        Page<Customer> eliteCustomers = customerRepository.findCustomersByStatusId(1L, PageRequest.of(0, 10));
        assertEquals(3, eliteCustomers.getTotalElements());
        List<Long> ids = eliteCustomers.map(Customer::getId).getContent();
        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(3L));
        assertTrue(ids.contains(4L));
    }

    @Test
    void testCountByIsActiveTrue() {
        long count = customerRepository.countByIsActiveTrue();
        assertEquals(5, count);
    }
    
}
