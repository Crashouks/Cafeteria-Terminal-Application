package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.MenuException;

/** Десерт: «десерт є різновидом позиції меню». Підкласів не має, тому final. */
public final class Dessert extends MenuItem {

    public static final int DEFAULT_WEIGHT_G = 120;

    private final int weightG;

    public Dessert(long id, String name, double price, int calories, int weightG) throws MenuException {
        super(id, name, price, calories);
        this.weightG = weightG;
        trace("Dessert(" + name + ", " + weightG + " г)");
    }

    public Dessert(long id, String name, double price, int calories) throws MenuException {
        this(id, name, price, calories, DEFAULT_WEIGHT_G);
    }

    public int getWeightG() {
        return weightG;
    }

    @Override
    public String getCategory() {
        return "Десерт";
    }

    @Override
    public String servingInfo() {
        return weightG + " г";
    }

    @Override
    public String toString() {
        return super.toString() + " | " + weightG + " г";
    }

    @Override
    public boolean sameContent(MenuItem o) {
        return super.sameContent(o) && weightG == ((Dessert) o).weightG;
    }
}
