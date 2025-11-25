package com.teste.sat.dtos;

public class StatusDTO {
    
    private Long id;

    private String name;

    public StatusDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
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

}
