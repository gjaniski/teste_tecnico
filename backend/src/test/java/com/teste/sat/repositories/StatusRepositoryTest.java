package com.teste.sat.repositories;

import com.teste.sat.models.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;

    @Test
    void testFindAllByIdIn() {
        List<Long> ids = List.of(1L, 3L, 4L);
        List<Status> statuses = statusRepository.findAllByIdIn(ids);

        assertEquals(3, statuses.size());

        assertTrue(statuses.stream().anyMatch(s -> s.getName().equals("ELITE")));
        assertTrue(statuses.stream().anyMatch(s -> s.getName().equals("VIP")));
        assertTrue(statuses.stream().anyMatch(s -> s.getName().equals("PREMIUM")));
    }

    @Test
    void testFindById() {
        Optional<Status> status = statusRepository.findById(2L);
        assertTrue(status.isPresent());
        assertEquals("PRIME", status.get().getName());
    }

    @Test
    void testFindAll() {
        List<Status> allStatuses = statusRepository.findAll();
        assertEquals(4, allStatuses.size());
    }

}
