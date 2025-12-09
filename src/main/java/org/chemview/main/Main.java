package org.chemview.main;

import org.chemview.api.periodicTableEndpoint.periodicTableEndpoint;
import static org.chemview.api.periodicTableEndpoint.periodicTableEndpoint.createElements;
import static org.chemview.api.pubChemEndpoint.getChemicalInformation;

public class Main {

    public static void main(String[] args) throws Exception {
        createElements();

        StringBuilder pubChemResponse = getChemicalInformation();

        System.out.println("Response: \n" + pubChemResponse);

        StringBuilder response = periodicTableEndpoint.makePeriodicTableRequest();

        System.out.println("Response: \n" + response);
    }
}