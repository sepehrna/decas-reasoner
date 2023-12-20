package com.decas.reasoner.service;

import java.io.IOException;

public interface OntologyService {

    void save() throws IOException;

    void reload() throws IOException, InterruptedException;

}
