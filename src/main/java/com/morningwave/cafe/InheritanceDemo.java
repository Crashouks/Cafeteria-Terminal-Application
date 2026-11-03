package com.morningwave.cafe;

import com.morningwave.cafe.discount.PercentDiscount;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.Combo;
import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.HotDrink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.service.MenuService;

/** Демонстрація ЛР6: ієрархія MenuItem → Drink → HotDrink, MenuItem → Dessert. */
public class InheritanceDemo {

    public static void run() {
        System.out.println("=== ЛР6: ієрархія класів ===");
        try {
            demoConstructorChain();
            MenuItem[] items = demoPolymorphism();
            demoStaticVsDynamicType(items);
            demoEquals();
            demoComposition(items);
            demoSealedSwitch(items);
        } catch (MenuException e) {
            System.out.println("Помилка демонстрації: " + e.getMessage());
        }
    }

    private static void demoConstructorChain() throws MenuException {
        System.out.println("--- Ланцюжок конструкторів: new HotDrink(\"Капучино\", 60, 120) ---");
        MenuItem.setTraceConstructors(true);
        new HotDrink(1, "Капучино", 60, 120);
        MenuItem.setTraceConstructors(false);
        System.out.println("Спочатку HotDrink(3 арг.) → this(...) → HotDrink(4 арг.) → this(...) → HotDrink(5 арг.) → super(...) → Drink → MenuItem;");
        System.out.println("тіла виконуються від базового класу до похідного.");
    }

    private static MenuItem[] demoPolymorphism() throws MenuException {
        System.out.println("--- Поліморфізм: масив MenuItem[] ---");
        MenuItem[] items = {
                new Drink(2, "Лимонад", 40, 90, 300),
                new HotDrink(3, "Американо", 45, 5, 250, 85),
                new Dessert(4, "Чізкейк", 85, 380, 150)
        };
        for (MenuItem item : items) {
            // Статичний тип змінної — MenuItem, а виклик іде за динамічним типом об'єкта
            System.out.println(item + "  → порція: " + item.servingInfo() + ", легка: " + item.isLight());
        }
        return items;
    }

    private static void demoStaticVsDynamicType(MenuItem[] items) {
        System.out.println("--- Статичний і динамічний тип, instanceof ---");
        for (MenuItem item : items) {
            // item.getVolumeMl() не скомпілюється: у MenuItem такого методу немає (статичний тип).
            String result;
            if (item instanceof Drink drink) {
                result = "це Drink, об'єм " + drink.getVolumeMl() + " мл";
            } else {
                result = "це не напій";
            }
            System.out.println(item.getName() + ": статичний тип MenuItem, динамічний "
                    + item.getClass().getSimpleName() + " → " + result);
        }
    }

    private static void demoEquals() throws MenuException {
        System.out.println("--- equals() за id та sameContent(): симетрія ---");
        Drink drink = new Drink(5, "Чай", 30, 2, 250);
        HotDrink hot = new HotDrink(6, "Чай", 30, 2, 250, 80);
        System.out.println("Реалізація з getClass():  drink.sameContent(hot) = " + drink.sameContent(hot)
                + ", hot.sameContent(drink) = " + hot.sameContent(drink) + "  (симетрично)");
        Drink sameId = new Drink(5, "Чай (нова версія)", 35, 2, 250);
        System.out.println("equals() за id (ЛР7–9):   drink.equals(sameId) = " + drink.equals(sameId)
                + ", drink.equals(hot) = " + drink.equals(hot) + "  (інший id і клас)");

        LooseItem base = new LooseItem("Чай");
        LooseDrink sub = new LooseDrink("Чай", 250);
        System.out.println("Реалізація з instanceof:  base.equals(sub) = " + base.equals(sub)
                + ", sub.equals(base) = " + sub.equals(base) + "  (симетрію порушено!)");
    }

    private static void demoComposition(MenuItem[] items) throws MenuException {
        System.out.println("--- Композиція: Combo має напій і десерт ---");
        Combo combo = new Combo((Drink) items[1], (Dessert) items[2], new PercentDiscount(10));
        System.out.println(combo);
    }

    private static void demoSealedSwitch(MenuItem[] items) {
        System.out.println("--- switch + pattern matching по sealed-ієрархії ---");
        for (MenuItem item : items) {
            System.out.println(item.getName() + ": " + MenuService.details(item));
        }
    }

    // Навмисно «погана» ієрархія — лише щоб показати проблему equals() через instanceof.

    private static class LooseItem {
        final String name;

        LooseItem(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof LooseItem other && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private static class LooseDrink extends LooseItem {
        final int volume;

        LooseDrink(String name, int volume) {
            super(name);
            this.volume = volume;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof LooseDrink other && super.equals(o) && volume == other.volume;
        }

        @Override
        public int hashCode() {
            return 31 * super.hashCode() + volume;
        }
    }
}
