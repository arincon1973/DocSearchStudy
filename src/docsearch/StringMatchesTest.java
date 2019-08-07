package docsearch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringMatchesTest {

    StringMatches stringMatches = new StringMatches();
    String text = "This is my source of text today, and tomorrow";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @Test
    void regexStringMatch() {
        long result;
        try {
            result = stringMatches.regexStringMatcher.search("t", text);
            assertEquals(4, result, "Occurrences must be 4");
            result = stringMatches.regexStringMatcher.search(" t", text);
            assertEquals(3, result, "Occurrences must be 3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void simpleStringMatch() {
        long result;
        try {
            result = stringMatches.simpleStringMatcher.search("t", text);
            assertEquals(4, result, "Occurrences must be 4");
            result = stringMatches.simpleStringMatcher.search(" t", text);
            assertEquals(3, result, "Occurrences must be 3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}