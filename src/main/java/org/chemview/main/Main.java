package org.chemview.main;

import com.fasterxml.jackson.databind.JsonNode;
import org.chemview.api.periodicTableEndpoint.periodicTableEndpoint;
import static org.chemview.api.periodicTableEndpoint.periodicTableEndpoint.createElements;
import static org.chemview.api.pubChemEndpoint.getChemicalFormula;

public class Main {

    public static void main(String[] args) throws Exception {
        createElements();

        String pubChemResponse = getChemicalFormula("aspirin");

        System.out.println(pubChemResponse);

        JsonNode response = periodicTableEndpoint.makePeriodicTableRequest();

        System.out.println("Response: " + response.toPrettyString());
    }
}