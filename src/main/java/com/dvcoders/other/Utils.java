package com.dvcoders.other;

/**
 * @author Jake Loo (04 July, 2015)
 */
public class Utils {
    public static String orEmpty(String s) {
        return s == null ? "" : s;
    }

    public static boolean isEmpty(String s) {
        return orEmpty(s).isEmpty();
    }

    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }
}
