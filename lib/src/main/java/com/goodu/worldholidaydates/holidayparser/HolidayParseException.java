package com.goodu.worldholidaydates.holidayparser;

import javax.annotation.Nullable;

/**
 * An exception which will be thrown during evaluating opening hours
 */
public class HolidayParseException extends RuntimeException {

    /**
     * Construct an exception from a message
     * 
     * @param message
     */
    public HolidayParseException(String message) {
        this(message, null);
    }

    /**
     * Construct an exception from a message and an optional Rule where the
     * exception happenend
     * 
     * @param message
     * @param failingRule
     */
    public HolidayParseException(String message, @Nullable String failingRule) {
        super(messsageBuilder(message, failingRule));
    }

    /**
     * Build a custom message based on input message and optional Rule where
     * the exception happened
     * 
     * @param message a message
     * @param rule an optional Rule
     * @return the message for Exception
     */
    private static String messsageBuilder(String message, @Nullable String rule) {
        return message + ((rule != null) ? " at " + rule : "");
    }
}
