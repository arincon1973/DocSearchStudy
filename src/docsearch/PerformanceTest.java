package docsearch;

/**
 * Created by Adriana on 8/4/19.
 * */

import java.io.*;
import java.util.*;

/*
 * A program that performs a performance test on three different string matching algorithms: String Match, Regex and
 * Preprocessed file.  It runs 2M searches with random search pattern inputs using the following assumptions:
 * - The three algorithms are being used to find an exact match of text, so the patterns used didn’t include regular expression characters.
 * - The search patterns include characters in the set {[a-z],[A-Z],[0-9],[ ]}
 * - The search pattern is a string between 1 and 256 characters
 *
 * The files are loaded from list of files provided in a configuration file.  The configuration file name is provided as
 * the only parameter to the executable.
 *
 * @author      Adriana Rincon
 * */


public class PerformanceTest {

    public static void main(String[] args) throws IOException {
        InputStream is = null;
        InputStream targetStream = null;
        try {
            // Read arguments (config file)
            if (args.length < 1) {
                System.out.println("Invalid number of arguments");
                System.out.println("Usage: docsearch <config file name>");
                return;
            }

            // Load properties from config file (list of files to search)
            Properties prop = new Properties();
            String configFileName = args[0];
            try {
                is = new FileInputStream(configFileName);
            } catch (FileNotFoundException ex) {
                System.out.println("Config file not found: " + configFileName);
                return;
            }
            try {
                prop.load(is);
            } catch (IOException ex) {
                is.close();
                System.out.println("Error loading properties file: " + ex.getMessage());
                return;
            }

            String readers = prop.getProperty("app.readers");
            if (readers == null) {
                System.out.println("Properties files does not contain any files. Properties files format is: app.readers=<csv of file names>");
                return;
            }
            // Create the list of input files.
            String[] sources = readers.split(",");
            if (sources.length < 1) {
                System.out.println("Properties files does not contain any files. Properties files format is: app.readers=<csv of file names>");
                return;
            }

            // Load files content in memory
            StringMatches.loadSources(sources);

            StringMatches sm = new StringMatches();

            int executionTime1 = 0;
            int executionTime2 = 0;
            int executionTime3 = 0;

            for (int i = 0; i < 2000000; i++) {
                String searchTerm = RandomStrings.generateRandomString();
                System.out.println("Search term is: " + searchTerm);

                executionTime1 += sm.simpleStringMatch(searchTerm);
                executionTime2 += sm.regexStringMatch(searchTerm);
                executionTime3 += sm.preprocessedStringMatch(searchTerm);
            }
            System.out.println("Execution times:");

            System.out.println("String match: " + executionTime1);
            System.out.println("Regex match: " + executionTime2);
            System.out.println("Preprocessed match: " + executionTime3);

        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

}