package com.decas.reasoner.controller;

import com.decas.reasoner.service.OntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
    @PostMapping("/reload")
    public ResponseEntity<Boolean> reload() {
        try {
            ontologyService.reload();
            return ResponseEntity.ok(true);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}
