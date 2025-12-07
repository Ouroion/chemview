package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    static Dotenv dotenv = Dotenv.load();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static String apiKey = dotenv.get("PERIODIC_TABLE_API_KEY");
    static String ptURL = dotenv.get("PERIODIC_TABLE_URL");

    public static void main(String[] args) throws Exception {
        // Debug: Print configuration
        System.out.println("=== Configuration Debug ===");
        System.out.println("API URL: " + ptURL);
        System.out.println("API Key present: " + (apiKey != null && !apiKey.isEmpty()));
        System.out.println("API Key length: " + (apiKey != null ? apiKey.length() : 0));
        System.out.println("API Key first 10 chars: " + (apiKey != null && apiKey.length() > 10 ? apiKey.substring(0, 10) + "..." : apiKey));
        System.out.println("===========================\n");

        JsonNode response = makePeriodicTableRequest();
        System.out.println("Response: " + response.toPrettyString());
    }

    public static JsonNode makePeriodicTableRequest() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a periodic table element (symbol like 'H' or 'He')");

        String element = scan.nextLine().trim();

        String encodedElement = URLEncoder.encode(element, StandardCharsets.UTF_8);
        String ptURLParams = ptURL + "?name=" + encodedElement;

        System.out.println("Making request to: " + ptURLParams);

        URL url = new URL(ptURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-api-key", apiKey);
        conn.setRequestProperty("Accept", "application/json");

        // Debug: Print all request headers
        System.out.println("Request headers:");
        conn.getRequestProperties().forEach((key, value) ->
                System.out.println("  " + key + ": " + value)
        );

        return readResponse(conn);
    }

    private static JsonNode readResponse(HttpURLConnection conn) throws Exception {
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