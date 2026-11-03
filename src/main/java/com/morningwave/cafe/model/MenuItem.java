package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.InvalidCaloriesException;
import com.morningwave.cafe.exception.InvalidPriceException;
import com.morningwave.cafe.exception.MenuException;

public class MenuItem {

    public String name;
    public String category;
    public double price;
    public int calories;

    public MenuItem(String name, String category, double price, int calories) throws MenuException {
        if (price <= 0) {
            throw new InvalidPriceException("Ціна має бути більшою за 0", price);
        }
        if (calories < 0) {
            throw new InvalidCaloriesException("Калорійність не може бути від'ємною", calories);
        }
        this.name = name;
        this.category = category;
        this.price = price;
        this.calories = calories;
    }

    @Override
    public String toString() {
        return name + " | " + category + " | " + price + " грн | " + calories + " ккал";
    }

    public boolean sameAs(MenuItem other) {
        return name.equals(other.name) && category.equals(other.category)
                && price == other.price && calories == other.calories;
    }
}
