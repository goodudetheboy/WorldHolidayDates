package io.github.goodudetheboy.worldholidaydates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
     * Create a string from the content of input file in the ./resource folder
     * <p>
     * This code was adapted from the following Stack Overflow post:
     * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
     * </p>
     * 
     * @param filename filename in the ./resource folder
     * @return String created from content of a file
     * @throws IOException when can't find the file
     * @author erickson, https://stackoverflow.com/users/3474/erickson
     */
    public static String readFileFromResource(String filename) throws IOException {
        BufferedReader inputRules = new BufferedReader(new InputStreamReader(Utils.getFileFromResourceAsStream(filename), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = inputRules.readLine()) != null) {
            builder.append(line);
        }
        inputRules.close();
        return builder.toString();
    }

    /**
     * Gets an {@link InputStream} of a file from ./resource folder
     * 
     * @param fileName 
     * @return
     * @author mykong, https://mkyong.com/java/java-read-a-file-from-resources-folder/
     */
    public static InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the class
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
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
