package com.decas.reasoner.service;

import com.decas.reasoner.dto.PlanDto;

import java.io.IOException;
import java.util.List;

public interface OntologyService {

    void save() throws IOException;

    void reload() throws IOException, InterruptedException;

    void inferTemperature(String temperature, String individualName);

    void addPlan(PlanDto plan);

    List<PlanDto> fetchPlanByIndividual(String individualName);

    void doneAction(String individualName);
}
