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
    // Should be: https://api.apiverve.com/v1/periodictable
    static String ptURL = dotenv.get("PERIODIC_TABLE_URL");

    public static void main(String[] args) throws Exception {
        JsonNode response = makePeriodicTableRequest();
        System.out.println("Response: " + response.toPrettyString());
    }

    public static JsonNode makePeriodicTableRequest() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a periodic table element (symbol like 'H' or 'He')");

        String element = scan.nextLine();

        // APIVerve uses 'symbol' parameter, not 'name'
        String encodedElement = URLEncoder.encode(element, StandardCharsets.UTF_8);
        String ptURLParams = ptURL + "?name=" + encodedElement;

        System.out.println("Making request to: " + ptURLParams);

        URL url = new URL(ptURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-api-key", apiKey);
        // DON'T use setDoOutput for GET requests

        return readResponse(conn);
    }

    private static JsonNode readResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

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
                System.err.println("Error response: " + errorResponse.toString());
            } catch (Exception e) {
                System.err.println("Could not read error response");
            }
            throw new Exception("API request failed with response code: " + responseCode);
        }
    }
}