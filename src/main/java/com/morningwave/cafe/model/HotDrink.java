package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.MenuException;
import java.util.Objects;

/**
 * Гарячий напій: третій рівень ієрархії MenuItem → Drink → HotDrink.
 * final: від гарячого напою спадкоємців не планується, а запечатана ієрархія
 * вимагає, щоб кожен дозволений підклас був final, sealed або non-sealed.
 */
public final class HotDrink extends Drink {

    public static final int DEFAULT_TEMPERATURE_C = 80;

    private final int temperatureC;

    public HotDrink(String name, double price, int calories, int volumeMl, int temperatureC)
            throws MenuException {
        super(name, price, calories, volumeMl);
        this.temperatureC = temperatureC;
        trace("HotDrink(" + name + ", " + temperatureC + " °C)");
    }

    public HotDrink(String name, double price, int calories, int volumeMl) throws MenuException {
        this(name, price, calories, volumeMl, DEFAULT_TEMPERATURE_C);
    }

    public HotDrink(String name, double price, int calories) throws MenuException {
        this(name, price, calories, DEFAULT_VOLUME_ML);
    }

    public int getTemperatureC() {
        return temperatureC;
    }

    @Override
    public String getCategory() {
        return "Гарячий напій";
    }

    @Override
    public String servingInfo() {
        return super.servingInfo() + ", " + temperatureC + " °C";
    }

    @Override
    public String toString() {
        return super.toString() + " | " + temperatureC + " °C";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        return temperatureC == ((HotDrink) o).temperatureC;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), temperatureC);
    }
}
