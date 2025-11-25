package com.teste.sat.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import com.teste.sat.dtos.CustomerDTO;
import com.teste.sat.enums.ErrorMessage;
import com.teste.sat.exceptions.BusinessException;
import com.teste.sat.mappers.CustomerMapper;
import com.teste.sat.models.Customer;
import com.teste.sat.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StatusService statusService;
    
    @Spy 
    private CustomerMapper customerMapper;

    @Test
    void testFindByIdFound() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Maria");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO dto = customerService.findById(1L);

        assertNotNull(dto);
        assertEquals("Maria", dto.getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerDTO dto = customerService.findById(1L);

        assertNull(dto);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveSuccess() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmail("maria@teste.com");
        dto.setName("Maria");

        when(customerRepository.findByEmailAndIsActiveTrue("maria@teste.com")).thenReturn(Optional.empty());

        when(customerRepository.save(any(Customer.class)))
            .thenAnswer(invocation -> {
                Customer c = invocation.getArgument(0);
                c.setId(1L);
                return c;
            });

        CustomerDTO result = customerService.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testSaveEmailExistsThrowsException() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmail("maria@teste.com");

        Customer existing = new Customer();
        when(customerRepository.findByEmailAndIsActiveTrue("maria@teste.com"))
            .thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(BusinessException.class, () -> customerService.save(dto));
        assertEquals(ErrorMessage.CUSTOMER_EMAIL_ERROR, ex.getErrorMessage());
    }

    @Test
    void testUpdateSuccess() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setName("Maria");
        existing.setEmail("maria@teste.com");

        CustomerDTO dto = new CustomerDTO();
        dto.setName("Maria flor");
        dto.setEmail("maria.flor@teste.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        CustomerDTO result = customerService.update(1L, dto);

        assertEquals("Maria flor", result.getName());
        assertEquals("maria.flor@teste.com", result.getEmail());
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> customerService.update(1L, dto));
        assertEquals(ErrorMessage.CUSTOMER_NOT_FOUND, ex.getErrorMessage());
    }

    @Test
    void testDeleteSuccess() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        assertDoesNotThrow(() -> customerService.delete(1L));
        assertFalse(customer.getIsActive());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteNotFoundThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> customerService.delete(1L));
        assertEquals(ErrorMessage.CUSTOMER_NOT_FOUND, ex.getErrorMessage());
    }

    @Test
    void testFindPaginated() {
        List<Long> ids = List.of(1L, 2L);
        List<Customer> customers = new ArrayList<>();
        Customer c1 = new Customer(); c1.setId(1L); c1.setName("A");
        Customer c2 = new Customer(); c2.setId(2L); c2.setName("B");
        customers.add(c1); customers.add(c2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Long> idsPage = new PageImpl<>(ids, pageable, 2L);

        when(customerRepository.findActiveIds(pageable)).thenReturn(idsPage);
        when(customerRepository.findCustomersWithStatus(ids)).thenReturn(customers);
        when(customerRepository.countByIsActiveTrue()).thenReturn(2L);

        Page<CustomerDTO> page = customerService.findPaginated(pageable);

        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testFindByStatusPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Maria");

        Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 1L);

        when(customerRepository.findCustomersByStatusId(1L, pageable)).thenReturn(page);

        Page<CustomerDTO> result = customerService.findByStatusPaginated(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Maria", result.getContent().get(0).getName());

        verify(customerRepository, times(1)).findCustomersByStatusId(1L, pageable);
    }

}
