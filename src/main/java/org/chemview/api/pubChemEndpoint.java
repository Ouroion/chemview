package org.chemview.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class pubChemEndpoint {
    private static final String pcURL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static StringBuilder getChemicalInformation() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter chemical/element name (i.e. \"aspirin\" or \"iron\")");
        System.out.print("Chemical/Element Name: ");

        String input = scan.nextLine().trim();
        String pcURLParams = pcURL + input + "/JSON";

        System.out.println(ANSI_BLUE + "Getting information for " + input.toUpperCase() + " ..." + ANSI_RESET);

        URL url = new URL(pcURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        JsonNode response = readResponse(conn);

        return interpretResponse(response);
    }

    public static JsonNode readResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        System.out.println("Response Status:");
        System.out.println("\tResponse code: " + ANSI_YELLOW + responseCode + ANSI_RESET);
        System.out.println("\tResponse message: " + ANSI_YELLOW + conn.getResponseMessage() + ANSI_RESET);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return objectMapper.readTree(response.toString());
            }
        } else {
            // Read error response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                System.err.println("Error response body: " + errorResponse.toString());
            } catch (Exception e) {
                System.err.println("Could not read error response: " + e.getMessage());
            }
            throw new Exception("API request failed with response code: " + responseCode);
        }
    }

    public static StringBuilder interpretResponse(JsonNode response){
        JsonNode propsArray = response
                .get("PC_Compounds")
                .get(0)
                .get("props");

        String molecularFormula = null;
        String molecularWeight = null;
        String iupacName = null;
        StringBuilder str = new StringBuilder();

        for (JsonNode prop : propsArray) {
            String label = prop.get("urn").get("label").asText();

            if (label.equals("Molecular Formula")) {
                molecularFormula = prop.get("value").get("sval").asText();
                str.append("\tMolecular Formula: ").append(ANSI_YELLOW).append(molecularFormula).append("\n" + ANSI_RESET);
            } else if (label.equals("Molecular Weight")) {
                molecularWeight = prop.get("value").get("sval").asText();
                str.append("\tMolecular Weight: ").append(ANSI_YELLOW).append(molecularWeight).append("\n" + ANSI_RESET);
            } else if (label.equals("IUPAC Name")) {
                String name = prop.get("urn").get("name").asText();
                if (name.equals("Preferred")) {
                    iupacName = prop.get("value").get("sval").asText();
                    str.append("\tIUPAC Name: ").append(ANSI_YELLOW).append(iupacName).append("\n" + ANSI_RESET);
                }
            }
        }

        return str;
    }
}
