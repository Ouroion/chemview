package org.chemview.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chemview.api.periodicTableEndpoint.periodicTableEndpoint;
import static org.chemview.api.periodicTableEndpoint.periodicTableEndpoint.createElements;
import static org.chemview.api.pubChemEndpoint.getChemicalFormula;

public class Main {

    public static void main(String[] args) throws Exception {
        createElements();

        //String pubChemResponse = getChemicalFormula("aspirin");
        JsonNode pubChemResponse = getChemicalFormula();

        JsonNode propsArray = pubChemResponse
                .get("PC_Compounds")
                .get(0)
                .get("props");

        String molecularFormula = null;
        String molecularWeight = null;
        String iupacName = null;

        for (JsonNode prop : propsArray) {
            String label = prop.get("urn").get("label").asText();

            if (label.equals("Molecular Formula")) {
                molecularFormula = prop.get("value").get("sval").asText();
            } else if (label.equals("Molecular Weight")) {
                molecularWeight = prop.get("value").get("sval").asText();
            } else if (label.equals("IUPAC Name")) {
                String name = prop.get("urn").get("name").asText();
                if (name.equals("Preferred")) {
                    iupacName = prop.get("value").get("sval").asText();
                }
            }
        }

        System.out.println("Molecular Formula: " + molecularFormula);  // C9H8O4
        System.out.println("Molecular Weight: " + molecularWeight);    // 180.16
        System.out.println("IUPAC Name: " + iupacName);

        JsonNode response = periodicTableEndpoint.makePeriodicTableRequest();
        //JsonNode nameNode = response.get("data").get("name");
        //System.out.println("name " + nameNode.toPrettyString());

        System.out.println("Response: " + response.toPrettyString());
    }
}