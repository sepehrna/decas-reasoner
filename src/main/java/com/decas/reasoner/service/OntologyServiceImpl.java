package com.decas.reasoner.service;

import com.decas.reasoner.repository.OntologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OntologyServiceImpl implements OntologyService {

    private final OntologyRepository ontologyRepository;

    @Autowired
    public OntologyServiceImpl(OntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    @Override
    public void save() throws IOException {
        ontologyRepository.persist();
        ontologyRepository.load();
    }

    @Override
    public void reload() throws IOException {
        ontologyRepository.deleteGenerated();
        ontologyRepository.load();
    }
}
