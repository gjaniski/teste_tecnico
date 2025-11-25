package com.teste.sat.controllers;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.sat.dtos.CustomerDTO;
import com.teste.sat.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * HTTP GET /api/v1/customer
     * Lista os clientes.
     */
    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> listCustomers(Pageable pageable) {        
        Page<CustomerDTO> customersPage = customerService.findPaginated(pageable);        
        return ResponseEntity.ok(customersPage);
    }

    /**
     * HTTP GET /api/v1/customer/{id}
     * Busca um cliente específico pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);

        if (Objects.nonNull(customer)) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * HTTP POST /api/v1/customer
     * Cria um novo cliente.
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.save(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    /**
     * HTTP PUT /api/v1/customer/{id}
     * Atualiza todas as informações de um cliente pelo ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.update(id, customerDTO);

        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * HTTP DELETE /api/v1/customer/{id}
     * Remove um cliente pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP GET /api/v1/customer/status/{id}
     * Lista os clientes por status.
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<Page<CustomerDTO>> listCustomersByStatus(@PathVariable Long id, Pageable pageable) {        
        Page<CustomerDTO> customersPage = customerService.findByStatusPaginated(id, pageable);        
        return ResponseEntity.ok(customersPage);
    }

}
