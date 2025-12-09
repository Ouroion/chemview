package org.chemview.main;

import org.chemview.api.periodicTableEndpoint.element;
import org.chemview.api.periodicTableEndpoint.periodicTableEndpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.chemview.api.periodicTableEndpoint.periodicTableEndpoint.createElements;
import static org.chemview.api.pubChemEndpoint.getChemicalInformation;

public class Main {
    private static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws Exception {
        printBanner();

        createElements();

        StringBuilder pubChemResponse = getChemicalInformation();

        System.out.println("Response: \n" + pubChemResponse);

        StringBuilder response = periodicTableEndpoint.makePeriodicTableRequest();

        System.out.println("Response: \n" + response);
    }

    public static void printBanner(){
        File file = new File("src/main/java/org/chemview/resources/banner.txt");

        try{
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                System.out.println(ANSI_YELLOW + scan.nextLine() + ANSI_RESET);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}