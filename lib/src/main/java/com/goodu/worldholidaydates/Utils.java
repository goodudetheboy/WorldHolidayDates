package com.goodu.worldholidaydates;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import javax.annotation.Nonnull;


public class Utils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Private constructor to prevent instantiation
     */
    private Utils() {
        // empty
    }

    /**
     * Create a string from the content of input file, encoded by input charset
     * <p>
     * This code was adapted from the following Stack Overflow post:
     * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
     * 
     * @param path path to the file
     * @param encoding the charset to use
     * @return String created from content of a file
     * @throws IOException
     * @author erickson, https://stackoverflow.com/users/3474/erickson
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

        /**
     * Capitalize a string
     * 
     * @param s string to capitalize
     * @return capitalized string
     * @author Simon Poole
     */
    public static String capitalize(String s) {
        char[] c = s.toLowerCase(Locale.US).toCharArray();
        if (c.length > 0) {
            c[0] = Character.toUpperCase(c[0]);
            return new String(c);
        }
        return s;
    }

    /**
     * Make a safe comparison catching parse errors
     * 
     * @param token the current token
     * @param lower lower bounds
     * @param upper upper bounds
     * @return true if in bounds and is an int
     * @author Simon Pool
     */
    public static boolean between(@Nonnull String token, int lower, int upper) {
        try {
            int temp = Integer.parseInt(token);
            return temp >= lower && temp <= upper;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
