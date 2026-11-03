class MenuException extends Exception {

    public MenuException(String message) {
        super(message);
    }
}

class InvalidPriceException extends MenuException {

    public double value;

    public InvalidPriceException(String message, double value) {
        super(message);
        this.value = value;
    }
}

class InvalidCaloriesException extends MenuException {

    public int value;

    public InvalidCaloriesException(String message, int value) {
        super(message);
        this.value = value;
    }
}
