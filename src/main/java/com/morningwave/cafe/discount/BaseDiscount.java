package com.morningwave.cafe.discount;

public abstract class BaseDiscount implements DiscountStrategy, Describable {

    protected double notNegative(double value) {
        return value < 0 ? 0 : value;
    }

    public String describe() {
        return getName() + ": 100 грн -> " + calculate(100) + " грн";
    }
}
