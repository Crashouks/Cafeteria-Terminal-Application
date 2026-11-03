package com.morningwave.cafe.discount;

public class FixedDiscount extends BaseDiscount {

    private double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    public double calculate(double price) {
        return notNegative(price - amount);
    }

    public String getName() {
        return "Знижка " + amount + " грн";
    }
}
