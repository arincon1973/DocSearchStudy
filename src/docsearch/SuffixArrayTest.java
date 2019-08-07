package docsearch;

import static org.junit.jupiter.api.Assertions.*;

class SuffixArrayTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void search() {
        String text = "This is my source of text today, and tomorrow";

        SuffixArray suffixarray = new SuffixArray(text);
        suffixarray.createSuffixArray();

        // assert statements
        assertEquals(4, suffixarray.search("t"), "Occurrences must be 4");
        assertEquals(3, suffixarray.search(" t"), "Occurrences must be 3");
    }


}

