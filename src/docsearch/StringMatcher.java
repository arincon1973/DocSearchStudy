package docsearch;

/*
 * Interface for String Matchers
 *
 * @author      Adriana Rincon
 * */

public interface StringMatcher {
    int matchesCount(int index, String pattern);
}
