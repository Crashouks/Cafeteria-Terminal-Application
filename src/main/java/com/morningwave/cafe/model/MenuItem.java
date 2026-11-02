package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.InvalidCaloriesException;
import com.morningwave.cafe.exception.InvalidPriceException;
import com.morningwave.cafe.exception.MenuException;
import java.util.Objects;

/**
 * Базовий клас ієрархії меню. Абстрактний: «просто позиція меню» не існує —
 * у кафетерії завжди є напій або десерт. Запечатаний: дозволені лише напій і десерт.
 *
 * Раніше тип позиції визначався рядковим полем category ("Напій", "Десерт");
 * тепер тип — це сам клас, а категорію повертає абстрактний метод getCategory().
 */
public abstract sealed class MenuItem permits Drink, Dessert {

    /** Вмикає повідомлення в конструкторах (для демонстрації ланцюжка викликів). */
    private static boolean traceConstructors = false;

    private final String name;
    private final double price;
    private final int calories;

    protected MenuItem(String name, double price, int calories) throws MenuException {
        if (price <= 0) {
            throw new InvalidPriceException("Ціна має бути більшою за 0", price);
        }
        if (calories < 0) {
            throw new InvalidCaloriesException("Калорійність не може бути від'ємною", calories);
        }
        this.name = name;
        this.price = price;
        this.calories = calories;
        trace("MenuItem(" + name + ")");
    }

    public static void setTraceConstructors(boolean enabled) {
        traceConstructors = enabled;
    }

    protected static void trace(String message) {
        if (traceConstructors) {
            System.out.println("   [конструктор] " + message);
        }
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    /**
     * Абстрактний, бо базовий клас не знає, як вимірюється порція: напій — у мілілітрах,
     * десерт — у грамах. Змістовної реалізації «за замовчуванням» немає, тому кожен
     * підклас зобов'язаний дати свою.
     */
    public abstract String servingInfo();

    /** Категорія як властивість типу, а не вільний рядок, який можна ввести з помилкою. */
    public abstract String getCategory();

    /**
     * final: правило «легка позиція» — бізнес-правило кафетерію (менше 100 ккал).
     * Підкласи не повинні його змінювати, інакше фільтр «легких» позицій дав би різні
     * результати для різних типів.
     */
    public final boolean isLight() {
        return calories < 100;
    }

    @Override
    public String toString() {
        return name + " | " + getCategory() + " | " + price + " грн | " + calories + " ккал";
    }

    /**
     * Вибір getClass() (а не instanceof): підкласи додають власні поля, тож позиції
     * різних типів ніколи не мають вважатися рівними. Це зберігає симетрію
     * a.equals(b) == b.equals(a). Докладніше — у README та InheritanceDemo.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItem other = (MenuItem) o;
        return name.equals(other.name)
                && Double.compare(price, other.price) == 0
                && calories == other.calories;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, calories);
    }
}
