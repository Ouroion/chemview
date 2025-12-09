package org.chemview.api.periodicTableEndpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.io.File;

public class periodicTableEndpoint {

    private static final Dotenv dotenv = Dotenv.load();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String apiKey = dotenv.get("PERIODIC_TABLE_API_KEY");
    private static final String ptURL = dotenv.get("PERIODIC_TABLE_URL");
    private static final ArrayList<element> arr = new ArrayList<element>();

    public periodicTableEndpoint(){}

    public static void createElements(){
        String[] arrSplit;
        String line = "";
        String name = "";
        String symbol = "";

        File file = new File("src/main/java/org/chemview/resources/elements.txt");

        try{
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                line = scan.nextLine();
                arrSplit = line.split(" ");
                name = arrSplit[1];
                symbol = arrSplit[0];
                arr.add(new element(name, symbol));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String checkStringSymbol(String input){
        if(input.length() == 1 || input.length() == 2) {
            int i = 0;
            while(i < arr.size()){
                if(Objects.equals(arr.get(i).getSymbol(), input)){
                    return arr.get(i).getName();
                }
                else{
                    i++;
                }
            }
            return input + "is not a valid symbol in the periodic table.";
        }
        return input;
    }

    public static JsonNode makePeriodicTableRequest() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a periodic table element (i.e. \"hydrogen\" or \"H\")");

        String input = scan.nextLine().trim();
        String element =  checkStringSymbol(input);

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
