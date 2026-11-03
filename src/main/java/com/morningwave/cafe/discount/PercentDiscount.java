package com.morningwave.cafe.discount;

public class PercentDiscount extends BaseDiscount {

    private final double percent;

    public PercentDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double calculate(double price) {
        return notNegative(price - price * percent / 100);
    }

    @Override
    public String getName() {
        return "Знижка " + percent + "%";
    }
}
