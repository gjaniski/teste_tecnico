package com.teste.sat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.sat.dtos.StatusDTO;
import com.teste.sat.services.StatusService;

    
@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    @Autowired
    private StatusService statusService;
    
    /**
     * HTTP GET /api/v1/status
     * Lista todos os status.
     */
    @GetMapping
    public ResponseEntity<List<StatusDTO>> findAll() {
        List<StatusDTO> status = statusService.findAll();
        return ResponseEntity.ok(status);
    }

}
