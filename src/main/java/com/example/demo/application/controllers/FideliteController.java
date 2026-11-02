package com.example.demo.application.controllers;

import com.example.demo.application.dto.PointsResponse;
import com.example.demo.domain.services.FideliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/clients/{id}/points")
public class FideliteController {

    @Autowired
    private FideliteService fideliteService;

    @GetMapping
    public ResponseEntity<PointsResponse> consulterPoints(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(fideliteService.consulterPoints(id));
    }
}
