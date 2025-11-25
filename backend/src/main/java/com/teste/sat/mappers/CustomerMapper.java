package com.teste.sat.mappers;

import com.teste.sat.dtos.CustomerDTO;
import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.models.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());

        dto.setStatus(CollectionUtils.isEmpty(entity.getStatus())
            ? List.of()
            : entity.getStatus().stream()
                .map(st -> new StatusDTO(st.getId(), st.getName()))
                .collect(Collectors.toList()));        

        return dto;
    }

    public Customer toEntity(CustomerDTO dto) {
        Customer entity = new Customer();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setIsActive(true);

        return entity;
    }

}