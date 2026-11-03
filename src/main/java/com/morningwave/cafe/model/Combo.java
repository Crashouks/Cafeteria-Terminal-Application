package com.morningwave.cafe.model;

import com.morningwave.cafe.discount.DiscountStrategy;

/**
 * Комбо «напій + десерт» — приклад, де наслідування не підходить, а композиція так.
 * Комбо НЕ є напоєм і НЕ є десертом: воно МАЄ напій і МАЄ десерт.
 * Пояснення — у README (розділ ЛР6, рівень 3).
 */
public class Combo {

    private final Drink drink;
    private final Dessert dessert;
    private final DiscountStrategy discount;

    public Combo(Drink drink, Dessert dessert, DiscountStrategy discount) {
        this.drink = drink;
        this.dessert = dessert;
        this.discount = discount;
    }

    public double getPrice() {
        return discount.calculate(drink.getPrice() + dessert.getPrice());
    }

    public int getCalories() {
        return drink.getCalories() + dessert.getCalories();
    }

    @Override
    public String toString() {
        return "Комбо: " + drink.getName() + " + " + dessert.getName()
                + " (" + discount.getName() + ") = " + getPrice() + " грн, " + getCalories() + " ккал";
    }
}
