package com.morningwave.cafe.discount;

public interface DiscountStrategy {

    double calculate(double price);

    default String getName() {
        return getClass().getSimpleName();
    }

    default double calculateTotal(double price, int quantity) {
        return calculate(price) * quantity;
    }
}
