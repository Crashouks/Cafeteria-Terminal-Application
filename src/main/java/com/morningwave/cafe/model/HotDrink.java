package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.MenuException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Гарячий напій: третій рівень ієрархії MenuItem → Drink → HotDrink.
 * final: від гарячого напою спадкоємців не планується, а запечатана ієрархія
 * вимагає, щоб кожен дозволений підклас був final, sealed або non-sealed.
 */
@Entity
@DiscriminatorValue("HOT_DRINK")
public final class HotDrink extends Drink {

    public static final int DEFAULT_TEMPERATURE_C = 80;

    @Column(name = "temperature_c")
    private int temperatureC;

    protected HotDrink() {
        super();
    }

    public HotDrink(long id, String name, double price, int calories, int volumeMl, int temperatureC)
            throws MenuException {
        super(id, name, price, calories, volumeMl);
        this.temperatureC = temperatureC;
        trace("HotDrink(" + name + ", " + temperatureC + " °C)");
    }

    public HotDrink(long id, String name, double price, int calories, int volumeMl) throws MenuException {
        this(id, name, price, calories, volumeMl, DEFAULT_TEMPERATURE_C);
    }

    public HotDrink(long id, String name, double price, int calories) throws MenuException {
        this(id, name, price, calories, DEFAULT_VOLUME_ML);
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
    public boolean sameContent(MenuItem o) {
        return super.sameContent(o) && temperatureC == ((HotDrink) o).temperatureC;
    }
}
