package com.decas.reasoner.service;

import com.decas.reasoner.dto.SosaIndividual;

import java.io.IOException;
import java.util.List;

public interface LocationService {

    SosaIndividual defineLocation(SosaIndividual location) throws IOException;

    List<SosaIndividual> findAllLocation();

}
