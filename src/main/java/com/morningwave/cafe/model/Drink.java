package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.MenuException;

/** Напій: «напій є різновидом позиції меню». Не final, бо має підклас HotDrink. */
public sealed class Drink extends MenuItem permits HotDrink {

    public static final int DEFAULT_VOLUME_ML = 250;

    private final int volumeMl;

    public Drink(long id, String name, double price, int calories, int volumeMl) throws MenuException {
        super(id, name, price, calories);
        this.volumeMl = volumeMl;
        trace("Drink(" + name + ", " + volumeMl + " мл)");
    }

    /** Делегування через this(...): ініціалізація описана в одному місці. */
    public Drink(long id, String name, double price, int calories) throws MenuException {
        this(id, name, price, calories, DEFAULT_VOLUME_ML);
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    @Override
    public String getCategory() {
        return "Напій";
    }

    @Override
    public String servingInfo() {
        return volumeMl + " мл";
    }

    @Override
    public String toString() {
        return super.toString() + " | " + volumeMl + " мл";
    }

    @Override
    public boolean sameContent(MenuItem o) {
        return super.sameContent(o) && volumeMl == ((Drink) o).volumeMl;
    }
}
