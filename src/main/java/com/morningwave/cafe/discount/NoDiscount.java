package com.morningwave.cafe.discount;

public class NoDiscount implements DiscountStrategy {

    public double calculate(double price) {
        return price;
    }

    public String getName() {
        return "Без знижки";
    }
}
