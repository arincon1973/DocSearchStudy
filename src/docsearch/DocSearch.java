package docsearch;

/**
 * Created by Adriana on 8/4/19.
 * */


import java.io.*;
import java.util.*;

/*
 * A program that performs string matching on a set of files. The files are loaded from list of files provided in a
 * configuration file.  The configuration file name is provided as the only parameter to the executable.
 *
 * @author      Adriana Rincon
* */

public class DocSearch {

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

            System.out.println("Enter the search term: ");

            // Using Scanner for Getting Input from User
            Scanner in = new Scanner(System.in);

            String searchTerm = in.nextLine();

            int methodNumber;

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("\nSearch Method: 1) String Match 2) Regular Expression 3) Indexed");

                if (scanner.hasNextInt()) {
                    methodNumber = scanner.nextInt();
                } else {
                    System.out.println("The input is not an integer");
                    return;
                }
                if (methodNumber < 1 || methodNumber > 3) {
                    System.out.println("Invalid choice");
                    return;
                }

            }

            // Load files content in memory
            StringMatches.loadSources(sources);

            System.out.println("\nSearch results:\n");

            StringMatches sm = new StringMatches();

            // Invoke the correct method on the preselected files
            switch (methodNumber) {
                case 1:
                    sm.simpleStringMatch(searchTerm);
                    break;
                case 2:
                    sm.regexStringMatch(searchTerm);
                    break;
                case 3:
                    sm.preprocessedStringMatch(searchTerm);
                break;
                default:
                    System.out.println("Something went wrong");
                    return;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

}