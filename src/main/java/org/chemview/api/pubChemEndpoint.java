package org.chemview.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static org.chemview.api.periodicTableEndpoint.periodicTableEndpoint.readResponse;

public class pubChemEndpoint {
    private static final String pcURL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getChemicalFormula(String chemical) throws Exception {
        String response;

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter chemical name (i.e. \"aspirin\")");

        String input = scan.nextLine().trim();
        String pcURLParams = pcURL + input + "/property/MolecularFormula/TXT";

        URL url = new URL(pcURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try{
            // Read directly from the connection's input stream
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            response = result.toString();
            reader.close();
        } finally {
            conn.disconnect();
        }

        return response;
    }



}
