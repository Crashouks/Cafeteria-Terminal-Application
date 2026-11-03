package com.morningwave.cafe.service;

import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.HotDrink;
import com.morningwave.cafe.model.MenuItem;

public final class MenuService {

    private MenuService() {
    }

    public static void printMenu(String title, MenuItem[] menu) {
        System.out.println(title);
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public static void sortByPrice(MenuItem[] menu) {
        for (int i = 0; i < menu.length - 1; i++) {
            for (int j = 0; j < menu.length - 1 - i; j++) {
                if (menu[j].getPrice() > menu[j + 1].getPrice()) {
                    MenuItem temp = menu[j];
                    menu[j] = menu[j + 1];
                    menu[j + 1] = temp;
                }
            }
        }
    }

    /**
     * switch із pattern matching по запечатаній ієрархії. Компілятор знає, що MenuItem
     * дозволяє лише Drink і Dessert (HotDrink — підклас Drink), тому default не потрібен:
     * якщо додати новий дозволений підклас, цей switch перестане компілюватися,
     * доки його не обробити. Порядок case важливий: HotDrink — раніше за Drink.
     */
    public static String details(MenuItem item) {
        return switch (item) {
            case HotDrink hot -> "гарячий напій, " + hot.getVolumeMl() + " мл, " + hot.getTemperatureC() + " °C";
            case Drink drink -> "напій, " + drink.getVolumeMl() + " мл";
            case Dessert dessert -> "десерт, " + dessert.getWeightG() + " г";
        };
    }

    public static int linearSearch(MenuItem[] menu, MenuItem sample) {
        for (int i = 0; i < menu.length; i++) {
            if (menu[i].sameContent(sample)) {
                return i;
            }
        }
        return -1;
    }
}
