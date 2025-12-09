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

    public static JsonNode getChemicalFormula() throws Exception {
        String response;

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter chemical name (i.e. \"aspirin\")");

        String input = scan.nextLine().trim();
        String pcURLParams = pcURL + input + "/JSON";
        //String pcURLParams = pcURL + input + "/property/MolecularFormula/TXT";

        URL url = new URL(pcURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        /*try{
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
        }*/

        return readResponse(conn);
       // return response;
    }

    public static JsonNode readResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);
        System.out.println("Response message: " + conn.getResponseMessage());

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
}
