package com.teste.sat.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.teste.sat.dtos.CustomerDTO;
import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.enums.ErrorMessage;
import com.teste.sat.exceptions.BusinessException;
import com.teste.sat.mappers.CustomerMapper;
import com.teste.sat.models.Customer;
import com.teste.sat.models.Status;
import com.teste.sat.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private StatusService statusService;

    @Autowired
    private CustomerMapper customerMapper;

    public Page<CustomerDTO> findPaginated(Pageable pageable) {

        Page<Long> idsPage = customerRepository.findActiveIds(pageable);

        List<Customer> customers =
            customerRepository.findCustomersWithStatus(idsPage.getContent());

        List<CustomerDTO> dtos = customers.stream()
            .map(customerMapper::toDTO)
            .toList();

        long totalActive = customerRepository.countByIsActiveTrue();

        return new PageImpl<>(dtos, pageable, totalActive);
    }

    public CustomerDTO findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(entity -> customerMapper.toDTO(entity)).orElse(null);
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        try {
            Optional<Customer> customerOpt = customerRepository.findByEmailAndIsActiveTrue(customerDTO.getEmail());
            if(customerOpt.isPresent()) {
                throw new BusinessException(ErrorMessage.CUSTOMER_EMAIL_ERROR);
            }
 
            Set<Status> status = new HashSet<>();
            if (!CollectionUtils.isEmpty(customerDTO.getStatus())) {
                List<Long> statusIds = customerDTO.getStatus().stream()
                    .map(StatusDTO::getId)
                    .collect(Collectors.toList());

                status = new HashSet<>(statusService.findAllByIdIn(statusIds));
            }

            Customer customer = customerMapper.toEntity(customerDTO);
            customer.setStatus(status);
            customerRepository.save(customer);

            return customerMapper.toDTO(customer);
        } catch(BusinessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new BusinessException(ErrorMessage.CUSTOMER_SAVE_ERROR);
        }
    }

    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        try {
            Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessage.CUSTOMER_NOT_FOUND));

            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            
            if (CollectionUtils.isEmpty(customerDTO.getStatus())) {
                customer.setStatus(new HashSet<>());
            } else {
                List<Long> statusIds = customerDTO.getStatus()
                    .stream()
                    .map(StatusDTO::getId)
                    .collect(Collectors.toList());
                
                List<Status> statusList = statusService.findAllByIdIn(statusIds);

                Set<Long> idsEncontrados = statusList.stream()
                    .map(Status::getId)
                    .collect(Collectors.toSet());

                Set<Long> idsRecebidos = new HashSet<>(statusIds);

                Set<Long> idsNaoEncontrados = new HashSet<>(idsRecebidos);
                idsNaoEncontrados.removeAll(idsEncontrados);

                String nomesNaoEncontrados = customerDTO.getStatus().stream()
                    .filter(s -> idsNaoEncontrados.contains(s.getId()))
                    .map(StatusDTO::getName)
                    .collect(Collectors.joining(", "));

                if (!idsNaoEncontrados.isEmpty()) {
                    throw new BusinessException(
                        ErrorMessage.STATUS_NOT_FOUND,
                        ErrorMessage.STATUS_ID_INVALID.getMessage().concat(nomesNaoEncontrados)
                    );
                }

                customer.setStatus(new HashSet<>(statusList));
            }

            Customer updated = customerRepository.save(customer);
            return customerMapper.toDTO(updated);
        } catch(BusinessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new BusinessException(ErrorMessage.CUSTOMER_UPDATE_ERROR);
        }
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorMessage.CUSTOMER_NOT_FOUND));
            
        try {
            customer.setIsActive(false);
            customerRepository.save(customer);
        } catch(Exception ex) {
            throw new BusinessException(ErrorMessage.CUSTOMER_DELETE_ERROR);
        }
    }

    public Page<CustomerDTO> findByStatusPaginated(Long statusId, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findCustomersByStatusId(statusId, pageable);
        return customerPage.map(customer -> {
            return customerMapper.toDTO(customer); 
        });
    }
 
}
