package com.morningwave.cafe.discount;

public class FixedDiscount extends BaseDiscount {

    private final double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculate(double price) {
        return notNegative(price - amount);
    }

    @Override
    public String getName() {
        return "Знижка " + amount + " грн";
    }
}
