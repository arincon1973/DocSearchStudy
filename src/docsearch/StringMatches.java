package docsearch;

/**
 * Created by Adriana on 1/6/19.
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

public class StringMatches
{
    private static String[] sourceFileNames;
    public static String[] sources;
    private static SuffixArray[] preprocessedSources;
    private static boolean sourcesInitComplete = false;

    private class SimpleStringMatcher implements StringMatcher {
        @Override
        public int matchesCount(int index, String pattern) {
            String text = sources[index];
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

    private class RegexStringMatcher implements StringMatcher {
        @Override
        public int matchesCount(int index, String regexString) {
            String source = sources[index];
            if (source == null || source.trim().isEmpty()) // avoid NPE's, save CPU
                return 0;

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

    private SimpleStringMatcher simpleStringMatcher = new SimpleStringMatcher();
    private RegexStringMatcher regexStringMatcher = new RegexStringMatcher();
    private PreprocessedStringMatcher preprocessedStringMatcher = new PreprocessedStringMatcher();


    public long simpleStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.simpleStringMatcher, searchTerm);
    }

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


    public long regexStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.regexStringMatcher, searchTerm);
    }


    public long preprocessedStringMatch(String searchTerm) throws Exception {
        return executeMatch(this.preprocessedStringMatcher, searchTerm);    }

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