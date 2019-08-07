package docsearch;

/**
 * Created by Adriana on 8/4/19.
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

/*
 * A class to perform string searches on files using different search algorithms.
 *
 * @author      Adriana Rincon
 * */

public class StringMatches
{
    private static String[] sourceFileNames;
    public static String[] sources;
    private static SuffixArray[] preprocessedSources;
    private static boolean sourcesInitComplete = false;

    // Private classes that implement different search methods
    public class SimpleStringMatcher implements StringMatcher {
        @Override
        public int matchesCount(int index, String pattern) {
            String text = sources[index];
            if (text == null || text.trim().isEmpty()) // avoid NPE's, save CPU
                return 0;

            return search(pattern, text);
        }

        public int search(String pattern, String text) {
            int occurrences = 0;
            for (int i = 0; i <= (text.length() - pattern.length()); i++)
            {
                if (text.substring(i, (i + pattern.length())).equals(pattern))
                {
                    occurrences++;
                }
            }
            return occurrences;
        }
    }

    public class RegexStringMatcher implements StringMatcher {
        @Override
        public int matchesCount(int index, String regexString) {
            String source = sources[index];
            if (source == null || source.trim().isEmpty()) // avoid NPE's, save CPU
                return 0;

            return search(regexString, source);
        }

        public int search(String regexString, String source) {
            int totalMatches = 0;
            boolean matchFound = false;
            Pattern regex = Pattern.compile(regexString);
            Matcher regexMatcher = regex.matcher(source);

            while (regexMatcher.find()) {
                totalMatches++;
            }
            return totalMatches;
        }
    }

    private class PreprocessedStringMatcher implements StringMatcher {
        @Override
        public int matchesCount(int i, String searchTerm) {
            preprocessSource(i);
            return preprocessedSources[i].search(searchTerm);
        }
    }

    public SimpleStringMatcher simpleStringMatcher = new SimpleStringMatcher();
    public RegexStringMatcher regexStringMatcher = new RegexStringMatcher();
    public PreprocessedStringMatcher preprocessedStringMatcher = new PreprocessedStringMatcher();


    /**
     * Method to execute a search over a set of files and print total matches results in order of relevance
     *
     * @param  stringMatcher
     *         String matcher to use.  Defines the string matching algorithm to be used
     * @param  searchTerm
     *         String pattern to search in each of the files
     * @return  Elapsed time
     * @throws  Exception
     *          If files to search for have not been loaded as string in class variables
     */
    private static long executeMatch(StringMatcher stringMatcher, String searchTerm) throws Exception {
        if (sources == null) {
            System.out.println("No filenames loaded");
            throw new Exception("No filenames loaded");
        }

        long startTime = System.currentTimeMillis();

        int sourcesCount = sources.length;

        TreeMap<Integer, List<String>> results = new TreeMap<Integer, List<String>>();

        for (int i=0; i < sources.length; i++){
            int matches = stringMatcher.matchesCount(i, searchTerm);
            matches = matches * (-1);
            if (results.containsKey(matches)) {
                List<String> fileList = results.get(matches);
                fileList.add(sourceFileNames[i]);
            } else {
                List<String> fileList = new LinkedList<String>();
                fileList.add(sourceFileNames[i]);
                results.put(matches, fileList);
            }
        }

        Set<Map.Entry<Integer, List<String>>> mappings = results.entrySet();

        int matches;
        for(Map.Entry<Integer, List<String>> mapping : mappings){
            matches = mapping.getKey();
            matches = matches * (-1);
            List<String> filesList = mapping.getValue();
            for (String fileName : filesList) {
                System.out.println(fileName + " - " + matches + " matches\n");
            }
        }

        long endTime = System.currentTimeMillis();

        // get difference of two values
        long timeElapsed = endTime - startTime;

        System.out.println("Elapsed time: " + timeElapsed);

        return timeElapsed;
    }

    /**
     * Method to load a list of provided files into String class variables.
     *
     * @param  sourceFiles
     *         Array of strings containing names of files to be loaded
     *
     * @throws  Exception
     *          If no source file names are available in class variable
     */
    public static void loadSources(String[] sourceFiles) throws Exception {
        try
        {
            if (sourcesInitComplete) {
                return;
            }

            sourceFileNames = sourceFiles;
            sources = new String[sourceFileNames.length];
            preprocessedSources = new SuffixArray[sourceFileNames.length];

            if (sourceFileNames == null) {
                System.out.println("No filenames loaded");
                throw new java.lang.Exception("No filenames loaded");
            }
            for (int i=0; i < sourceFileNames.length; i++) {
                try
                {
                    StringMatches.sources[i] = new String ( Files.readAllBytes( Paths.get(sourceFileNames[i]) ) );
                }
                catch (IOException e)
                {
                    System.out.println("readAllBytes exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Performs search of provided pattern using the Regex matching algorithm
     *
     * @param  searchTerm
     *         String pattern to search in each of the files
     * @return  Elapsed time
     * @throws  Exception
     *          If files to search for have not been loaded as string in class variables
     */
    public long regexStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.regexStringMatcher, searchTerm);
    }

    /**
     * Performs search of provided pattern using the Simple Matcher algorithm
     *
     * @param  searchTerm
     *         String pattern to search in each of the files
     * @return  Elapsed time
     * @throws  Exception
     *          If files to search for have not been loaded as string in class variables
     */
    public long simpleStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.simpleStringMatcher, searchTerm);
    }

    /**
     * Performs search of provided pattern using the Preprocessed Matcher algorithm
     *
     * @param  searchTerm
     *         String pattern to search in each of the files
     * @return  Elapsed time
     * @throws  Exception
     *          If files to search for have not been loaded as string in class variables
     */
    public long preprocessedStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.preprocessedStringMatcher, searchTerm);    }

    /**
     * Preprocessed a String loaded in a class array by generating a suffix tree and storing it in a class array.
     *
     * @param  i
     *         Index of the string in the class array where the source test is loaded
     *
     */
    private static void preprocessSource(int i) {
        if (preprocessedSources[i] != null) {
            System.out.println("Preprocessing file: " + sourceFileNames[i]);
            return;
        }
        preprocessedSources[i] = new SuffixArray(sources[i]);
        preprocessedSources[i].createSuffixArray();
        return;
    }
}