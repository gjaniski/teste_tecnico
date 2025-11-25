package com.teste.sat.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.teste.sat.dtos.CustomerDTO;
import com.teste.sat.services.CustomerService;

import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;
    
    private CustomerDTO customer1;

    private CustomerDTO customer2;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();

        customer1 = new CustomerDTO();
        customer1.setId(1L);
        customer1.setName("João Pedrosa");
        customer1.setEmail("joao.pedrosa@teste.com");

        customer2 = new CustomerDTO();
        customer2.setId(2L);
        customer2.setName("Maria Silva");
        customer2.setEmail("maria.silva@teste.com");
    }

    @Test
    void testGetCustomerById() throws Exception {
        when(customerService.findById(1L)).thenReturn(customer1);

        mockMvc.perform(get("/api/v1/customer/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("João Pedrosa"))
            .andExpect(jsonPath("$.email").value("joao.pedrosa@teste.com"));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        when(customerService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/customer/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testListCustomersPaginated() throws Exception {
        Page<CustomerDTO> page = new PageImpl<>(List.of(customer1, customer2), PageRequest.of(0, 10), 5);
        when(customerService.findPaginated(any(Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get("/api/v1/customer")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("João Pedrosa"))
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.save(any(CustomerDTO.class))).thenReturn(customer1);

        mockMvc.perform(post("/api/v1/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customer1)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("João Pedrosa"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).delete(1L);

        mockMvc.perform(delete("/api/v1/customer/1"))
            .andExpect(status().isNoContent());
    }

}
