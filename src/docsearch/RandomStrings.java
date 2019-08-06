package docsearch;

import java.security.SecureRandom;
import java.util.Random;

/*
 * A utility class to generate random strings of lenghts between 1-256 characters. The alphabet used to generate the
 * string is: upper and lower case a-z characters, 0-9 digits and the ' ' character.
 *
 * @author      Adriana Rincon
 * */

public class RandomStrings {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPACE = " ";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER + SPACE;
    private static SecureRandom random = new SecureRandom();

    /**
     * Generate random string of length in [1-256]
     *
     */
    public static String generateRandomString() {
        int r1 = getRandomNumberInRange(1, 256);
        return generateRandomString(r1);
    }

    /**
     * Generate random string
     *
     * @param  length
     *         Lenght of the generated string
     * @return  Generated string
     * @throws  IllegalArgumentException
     *          If length is < 1
     */
    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException("Length is:" + length);

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);

        }

        return sb.toString();

    }

    /**
     * Generate random integer in a given range
     *
     * @param  min
     *         Lower boundary of generated number
     * @param  max
     *         Upper boundary of generated number
     * @return  Generated integer
     * @throws  IllegalArgumentException
     *          If max < min
     */
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
