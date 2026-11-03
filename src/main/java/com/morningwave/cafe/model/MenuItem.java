package com.morningwave.cafe.model;

import com.morningwave.cafe.exception.InvalidCaloriesException;
import com.morningwave.cafe.exception.InvalidPriceException;
import com.morningwave.cafe.exception.MenuException;

/**
 * Базовий клас ієрархії меню. Абстрактний: «просто позиція меню» не існує —
 * у кафетерії завжди є напій або десерт. Запечатаний: дозволені лише напій і десерт.
 *
 * Раніше тип позиції визначався рядковим полем category ("Напій", "Десерт");
 * тепер тип — це сам клас, а категорію повертає абстрактний метод getCategory().
 */
public abstract sealed class MenuItem extends BaseEntity permits Drink, Dessert {

    /** Вмикає повідомлення в конструкторах (для демонстрації ланцюжка викликів). */
    private static boolean traceConstructors = false;

    private final double price;
    private final int calories;

    protected MenuItem(long id, String name, double price, int calories) throws MenuException {
        super(id, name);
        if (price <= 0) {
            throw new InvalidPriceException("Ціна має бути більшою за 0", price);
        }
        if (calories < 0) {
            throw new InvalidCaloriesException("Калорійність не може бути від'ємною", calories);
        }
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
        return getName() + " | " + getCategory() + " | " + price + " грн | " + calories + " ккал";
    }

    /**
     * Порівняння за вмістом (усі поля, а не id) — колишній equals() з ЛР6. Потрібен для
     * пошуку за зразком (linearSearch), бо equals() тепер успадковано від BaseEntity і працює за id.
     * getClass() (а не instanceof) зберігає симетрію: підкласи додають власні поля.
     */
    public boolean sameContent(MenuItem o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return getName().equals(o.getName())
                && Double.compare(price, o.price) == 0
                && calories == o.calories;
    }
}
