package com.morningwave.cafe.discount;

public class PercentDiscount extends BaseDiscount {

    private double percent;

    public PercentDiscount(double percent) {
        this.percent = percent;
    }

    public double calculate(double price) {
        return notNegative(price - price * percent / 100);
    }

    public String getName() {
        return "Знижка " + percent + "%";
    }
}
