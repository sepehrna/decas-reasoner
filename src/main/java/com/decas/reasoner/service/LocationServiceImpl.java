package com.decas.reasoner.service;

import com.decas.reasoner.dto.SosaIndividual;
import com.decas.reasoner.repository.OntologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private final OntologyRepository ontologyRepository;

    @Autowired
    public LocationServiceImpl(OntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    @Override
    public SosaIndividual defineLocation(SosaIndividual location) throws IOException {
        String individualName = UUID.randomUUID().toString();
        ontologyRepository.insertIndividual(individualName, "Location", location.getLabel(), location.getDefinition(), location.getComment(), new HashMap<>());
        location.setName(individualName);
        return location;
    }

    @Override
    public List<SosaIndividual> findAllLocation() {
        List<List<SosaIndividual>> result = ontologyRepository.executeQuery(
                "SELECT ?locations WHERE { ?locations rdf:type decas:Location}"
        ).values().stream().toList();
        return !result.isEmpty() ? result.get(0) : new ArrayList<>();
    }
}
