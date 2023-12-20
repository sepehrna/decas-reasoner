package com.decas.reasoner.repository;

import com.decas.reasoner.dto.SosaIndividual;
import jakarta.annotation.PostConstruct;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DecasOntologyRepositoryImpl implements OntologyRepository {
    private OntModel ontology;
    private final String prefixes =
            "PREFIX owl: <" + DecasUri.OWL + ">"
                    + " PREFIX rdf: <" + DecasUri.RDF + ">"
                    + " PREFIX rdfs: <" + DecasUri.RDFS + ">"
                    + " PREFIX decas: <" + DecasUri.DECAS + ">"
                    + " PREFIX sosa: <" + DecasUri.SOSA + ">"
                    + " PREFIX ssn: <" + DecasUri.SSN + ">"
                    + " PREFIX skos: <" + DecasUri.SKOS + ">";

    @PostConstruct
    public void load() throws FileNotFoundException {
        String ontologyFile = "src/main/resources/generated-ontology.owl";
        File file = new File(ontologyFile);
        if (!file.exists()) {
            ontologyFile = "src/main/resources/decas-ontology-raw.owl";
        }
        ontology = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        FileInputStream fileInputStream = new FileInputStream(ontologyFile);

        String source = "http://www.semanticweb.org/decentralized-context-aware-ontology";
        ontology.read(fileInputStream, source, "RDF/XML");
    }

    public void deleteGenerated() throws IOException {
        ontology.close();
        String generatedFilePath = "src/main/resources/generated-ontology.owl";
        File toBeDeletedFile = new File(generatedFilePath);
        boolean deleted = toBeDeletedFile.delete();
        if (!deleted)
            throw new IOException("File cannot be deleted");
    }

    public void persist() throws IOException {
        String persistingFile = "src/main/resources/generated-ontology.jsonld";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(persistingFile);
        } catch (FileNotFoundException e) {
            File newFile = new File(persistingFile);
            if (!newFile.createNewFile()) {
                throw new IOException("There is a problem in generating node ontology file!!!");
            }
            fileOutputStream = new FileOutputStream(newFile);
        }
        ontology.write(fileOutputStream, "JSON-LD", "http://www.semanticweb.org/decentralized-context-aware-ontology");
        ontology.close();
        fileOutputStream.close();
    }

    private String buildUri(String entity) {
        return DecasUri.DECAS + entity;
    }

    public void insertIndividual(String individualId, String type, String label, String definition, String comment, Map<String, String> decasProperties) {

        String individualUri = buildUri(individualId);
        String typeUri = buildUri(type);

        Individual newIndividual = ontology.createIndividual(individualUri, ontology.createClass(typeUri));

        newIndividual.addProperty(RDFS.comment,
                ontology.createLiteral(comment, "en"));

        newIndividual.addProperty(RDFS.label,
                ontology.createLiteral(label, "en"));

        Property definitionProperty = ontology.createProperty(DecasUri.SKOS, "definition");
        newIndividual.addProperty(definitionProperty,
                ontology.createLiteral(definition, "en"));

    }

    public Map<String, List<SosaIndividual>> executeQuery(String queryString) {
        Query query = QueryFactory.create(prefixes + " " + queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, ontology);
        ResultSet results = qe.execSelect();
        Map<String, List<SosaIndividual>> resultMap = new HashMap<>();
        List<String> vars = results.getResultVars();
        for (String var : vars) {
            List<SosaIndividual> individualList = new ArrayList<>();
            while (results.hasNext()) {
                QuerySolution qs = results.nextSolution();
                RDFNode node = qs.get(var);
                if (node.isResource()) {
                    OntResource res = node.as(OntResource.class);
                    Property definitionProperty = ontology.getProperty(DecasUri.SKOS + "definition");
                    Statement definition = res.getProperty(definitionProperty, "en");
                    SosaIndividual sosaIndividual = SosaIndividual.builder()
                            .label(res.getLabel("en"))
                            .comment(res.getComment("en"))
                            .name(res.getLocalName())
                            .definition(definition.getString())
                            .build();
                    individualList.add(sosaIndividual);
                } else {
                    Literal lit = node.asLiteral();
                    SosaIndividual literal = SosaIndividual.builder()
                            .label(lit.getString())
                            .comment(lit.getString())
                            .name(lit.getString())
                            .definition(lit.getString())
                            .build();
                    individualList.add(literal);
                }
            }
            resultMap.put(var, individualList);
        }
        qe.close();

        return resultMap;
    }
}
