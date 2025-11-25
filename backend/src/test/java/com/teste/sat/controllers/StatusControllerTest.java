package com.teste.sat.controllers;

import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.services.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatusService statusService;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statusController).build();
    }

    @Test
    void testFindAll() throws Exception {
        List<StatusDTO> statusList = List.of(
            new StatusDTO(1L, "ELITE"),
            new StatusDTO(2L, "PRIME"),
            new StatusDTO(3L, "VIP")
        );

        when(statusService.findAll()).thenReturn(statusList);

        mockMvc.perform(get("/api/v1/status")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].name").value("ELITE"))
            .andExpect(jsonPath("$[1].name").value("PRIME"))
            .andExpect(jsonPath("$[2].name").value("VIP"));
    }

}
