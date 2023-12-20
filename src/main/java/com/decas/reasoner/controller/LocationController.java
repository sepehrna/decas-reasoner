package com.decas.reasoner.controller;

import com.decas.reasoner.dto.SosaIndividual;
import com.decas.reasoner.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/define-location")
    ResponseEntity<SosaIndividual> defineLocation(@RequestBody SosaIndividual location) {
        try {
            return ResponseEntity
                    .ok(locationService.defineLocation(location));
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    @GetMapping("/find-all-location")
    ResponseEntity<List<SosaIndividual>> findAllLocation(){
        return ResponseEntity
                .ok(locationService.findAllLocation());
    }

}
