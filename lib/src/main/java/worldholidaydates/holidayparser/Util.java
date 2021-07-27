package worldholidaydates.holidayparser;

import java.util.Locale;

public class Util {
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
}
