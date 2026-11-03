package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.MenuException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Десерт: «десерт є різновидом позиції меню». Підкласів не має, тому final. */
@Entity
@DiscriminatorValue("DESSERT")
public final class Dessert extends MenuItem {

    public static final int DEFAULT_WEIGHT_G = 120;

    @Column(name = "weight_g")
    private int weightG;

    protected Dessert() {
        super();
    }

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
