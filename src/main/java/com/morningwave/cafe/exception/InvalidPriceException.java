package com.morningwave.cafe.exception;

public class InvalidPriceException extends MenuException {

    public double value;

    public InvalidPriceException(String message, double value) {
        super(message);
        this.value = value;
    }
}
