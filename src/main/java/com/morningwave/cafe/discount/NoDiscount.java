package com.morningwave.cafe.discount;

public class NoDiscount implements DiscountStrategy {

    @Override
    public double calculate(double price) {
        return price;
    }

    @Override
    public String getName() {
        return "Без знижки";
    }
}
