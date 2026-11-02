package com.example.demo.application.controllers;

import com.example.demo.application.dto.RendezVousCreateRequest;
import com.example.demo.application.dto.RendezVousResponse;
import com.example.demo.domain.model.RendezVous;
import com.example.demo.domain.model.Statut;
import com.example.demo.domain.services.RendezVousService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/shops/{shop_id}/rendez-vous")
public class RendezVousController {

    @Autowired
    public RendezVousService rendezVousService;

    @PostMapping
    public ResponseEntity<RendezVousResponse> createRendezVous(@PathVariable("shop_id") Integer shop_id,
                                                               @Valid @RequestBody RendezVousCreateRequest request) {
        RendezVous created = rendezVousService.createRendezVous(shop_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(RendezVousResponse.from(created));
    };

    @GetMapping
    public ResponseEntity<List<RendezVousResponse>> listerRendezVous(@PathVariable("shop_id") Integer shop_id,
                                                                     @RequestParam(value = "client_id", required = false) Integer client_id,
                                                                     @RequestParam(value = "date", required = false) LocalDate date,
                                                                     @RequestParam(value = "statut", required = false) Statut statut) {
        List<RendezVousResponse> rendezVous = rendezVousService.listerRendezVous(shop_id, client_id, date, statut)
                .stream().map(RendezVousResponse::from).toList();
        return ResponseEntity.ok(rendezVous);
    };

    @PatchMapping(value = "/{id}/honorer")
    public ResponseEntity<RendezVousResponse> honorerRendezVous(@PathVariable("shop_id") Integer shop_id,
                                                                @PathVariable("id") Integer id) {
        RendezVous honore = rendezVousService.honorerRendezVous(shop_id, id);
        return ResponseEntity.ok(RendezVousResponse.from(honore));
    };

    @PatchMapping(value = "/{id}/annuler")
    public ResponseEntity<RendezVousResponse> annulerRendezVous(@PathVariable("shop_id") Integer shop_id,
                                                                @PathVariable("id") Integer id) {
        RendezVous annule = rendezVousService.annulerRendezVous(shop_id, id);
        return ResponseEntity.ok(RendezVousResponse.from(annule));
    };
}
