package com.teste.sat.services;

import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.models.Status;
import com.teste.sat.repositories.StatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    @Test
    void testFindAll() {
        List<Status> statuses = List.of(
            new Status(1L, "ELITE"),
            new Status(2L, "PRIME"),
            new Status(3L, "VIP")
        );

        when(statusRepository.findAll()).thenReturn(statuses);

        List<StatusDTO> result = statusService.findAll();

        assertEquals(3, result.size());
        assertEquals("ELITE", result.get(0).getName());
        assertEquals("PRIME", result.get(1).getName());
        assertEquals("VIP", result.get(2).getName());
    }

    @Test
    void testFindAllByIdIn() {
        List<Status> statuses = List.of(
            new Status(1L, "ELITE"),
            new Status(3L, "VIP")
        );

        when(statusRepository.findAllByIdIn(List.of(1L, 3L))).thenReturn(statuses);

        List<Status> result = statusService.findAllByIdIn(List.of(1L, 3L));

        assertEquals(2, result.size());
        assertEquals("ELITE", result.get(0).getName());
        assertEquals("VIP", result.get(1).getName());
    }

}
