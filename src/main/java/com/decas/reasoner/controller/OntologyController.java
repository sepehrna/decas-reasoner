package com.decas.reasoner.controller;

import com.decas.reasoner.dto.PlanDto;
import com.decas.reasoner.service.OntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class OntologyController {

    private final OntologyService ontologyService;

    @Autowired
    public OntologyController(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    @PostMapping("/save")
    public ResponseEntity<Boolean> save() {
        try {
            ontologyService.save();
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/reload")
    public ResponseEntity<Boolean> reload() {
        try {
            ontologyService.reload();
            return ResponseEntity.ok(true);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/infer-temperature")
    public ResponseEntity<Boolean> inferTemperature(@RequestParam String temperature, @RequestParam String individualName) {
        try {
            ontologyService.inferTemperature(temperature, individualName);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/fetch-actions")
    public ResponseEntity<List<PlanDto>> fetchActions(@RequestParam String individualName) {
        try {
            List<PlanDto> planList = ontologyService.fetchPlanByIndividual(individualName);
            return ResponseEntity.ok(planList);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/done-action")
    public ResponseEntity<Boolean> doneActions(@RequestParam String individualName) {
        try {
            ontologyService.doneAction(individualName);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
