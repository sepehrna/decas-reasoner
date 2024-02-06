package com.decas.reasoner.repository;

import com.decas.reasoner.dto.SosaIndividual;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OntologyRepository {

    void load() throws FileNotFoundException;

    void deleteGenerated() throws IOException;

    void persist() throws IOException;

    QueryResult executeSimpleQuery(String queryString);

    Map<String, List<SosaIndividual>> executeQuery(String queryString);

    void insertIndividual(String individualId, String type, String label, String definition, String comment, Map<String, String> characteristics);

}
