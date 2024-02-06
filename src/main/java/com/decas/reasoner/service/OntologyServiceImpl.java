package com.decas.reasoner.service;

import com.decas.reasoner.dto.PlanDto;
import com.decas.reasoner.repository.OntologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OntologyServiceImpl implements OntologyService {

    private final OntologyRepository ontologyRepository;

    private final List<PlanDto> planList = new ArrayList<>();

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

    @Override
    public void inferTemperature(String temperature, String individualName) {
        double min = 18;
        double max = 20;
        try {
            ontologyRepository.executeSimpleQuery(
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX decas: <http://www.semanticweb.org/decentralized-context-aware-ontology#>\n" +
                            "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "\n" +
                            "SELECT ?minTemperature\n" +
                            "WHERE {\n" +
                            "  ?sensorIndividual rdf:type decas:Temperature_Sensor.\n" +
                            "  ?sensorIndividual sosa:observes ?materialIndividual.\n" +
                            "  ?materialIndividual rdf:type decas:Processing_Material.\n" +
                            "  ?materialIndividual decas:min_temperature ?minTemperature.\n" +
                            "  ?materialIndividual decas:max_temperature ?maxTemperature.\n" +
                            "  FILTER (str(?sensorIndividual) = \"http://www.semanticweb.org/decentralized-context-aware-ontology#" + individualName + "\")\n" +
                            "}"
            ).getTextResult();
        } catch (Exception e) {
        }

        double temp = Double.parseDouble(temperature);
        String targetIndividual = "a48538a8-76a6-48ca-ae66-7620957fb9a6";
        List<PlanDto> offPlans = fetchPlanByIndividual(targetIndividual)
                .stream()
                .filter(planDto -> planDto.getDeviceId().equals(targetIndividual)
                        && planDto.getAction().equals("off"))
                .toList();
        List<PlanDto> onPlans = fetchPlanByIndividual(targetIndividual)
                .stream()
                .filter(planDto -> planDto.getDeviceId().equals(targetIndividual)
                        && planDto.getAction().equals("on"))
                .toList();
        if (temp < min || temp > max) {
            removePreviousPlan(targetIndividual);
            if (offPlans.isEmpty()) {
                addPlan(PlanDto.builder().action("off").deviceId(targetIndividual).build());
            }
        }
        if (temp >= min && temp <= max) {
            removePreviousPlan(targetIndividual);
            if (onPlans.isEmpty()) {
                addPlan(PlanDto.builder().action("on").deviceId(targetIndividual).build());
            }
        }
    }

    private void removePreviousPlan(String targetIndividual) {
        planList.removeIf(planDto -> planDto.getDeviceId().equals(targetIndividual));
    }

    @Override
    public void addPlan(PlanDto plan) {
        planList.add(plan);
    }

    @Override
    public List<PlanDto> fetchPlanByIndividual(String individualName) {
        return planList.stream().filter(plan -> plan.getDeviceId().equals(individualName)).collect(Collectors.toList());
    }

    @Override
    public void doneAction(String individualName) {
        removePreviousPlan(individualName);
    }
}
