package com.jukusoft.mmo.engine.exception;

/**
 * Thrown when a pushed screen is not found.
 */
public class ScreenNotFoundException extends RuntimeException {

    public ScreenNotFoundException(String message) {
        super(message);
    }

}
