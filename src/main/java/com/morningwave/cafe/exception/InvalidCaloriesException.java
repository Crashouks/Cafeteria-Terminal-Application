package com.morningwave.cafe.exception;

public class InvalidCaloriesException extends MenuException {

    public int value;

    public InvalidCaloriesException(String message, int value) {
        super(message);
        this.value = value;
    }
}
