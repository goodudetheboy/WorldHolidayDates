package com.goodu.worldholidaydates.holidaydata;

/**
 * An exception class to handle errors that occur during the initialization of
 * the holiday data.
 */
public class HolidayInitializationException extends RuntimeException {
    /**
     * Exception constructor with a message.
     */
    public HolidayInitializationException(String message) {
        super(message);
    }

    /**
     * Exception constructor with a message and an inner exception.
     * 
     * @param message the message
     * @param cause the inner exception
     */
    public HolidayInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
