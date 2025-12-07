package org.chemview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    static Dotenv dotenv = Dotenv.load();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static String apiKey = dotenv.get("PERIODIC_TABLE_API_KEY");
    static String ptURL = dotenv.get("PERIODIC_TABLE_URL");

    public static void main(String[] args) throws Exception {
        JsonNode response = periodicTable.makePeriodicTableRequest();
        System.out.println("Response: " + response.toPrettyString());
    }
}