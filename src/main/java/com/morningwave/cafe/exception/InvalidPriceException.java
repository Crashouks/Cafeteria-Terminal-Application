package com.morningwave.cafe.exception;

public class InvalidPriceException extends MenuException {

    public final double value;

    public InvalidPriceException(String message, double value) {
        super(message);
        this.value = value;
    }
}
