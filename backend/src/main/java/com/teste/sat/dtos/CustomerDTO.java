package com.teste.sat.dtos;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CustomerDTO {
 
    private Long id;

    @NotBlank(message = "O campo Nome é obrigatório!")
    private String name;

    @NotBlank(message = "O campo Email é obrigatório!")
    @Email(message = "Email inválido!")
    private String email;

    private List<StatusDTO> status;
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setStatus(List<StatusDTO> status) {
        this.status = status;
    }

    public List<StatusDTO> getStatus() {
        return status;
    }

}
