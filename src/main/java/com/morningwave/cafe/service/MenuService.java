package com.morningwave.cafe.service;

import com.morningwave.cafe.model.MenuItem;

public class MenuService {

    public static void printMenu(String title, MenuItem[] menu) {
        System.out.println(title);
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public static void sortByPrice(MenuItem[] menu) {
        for (int i = 0; i < menu.length - 1; i++) {
            for (int j = 0; j < menu.length - 1 - i; j++) {
                if (menu[j].price > menu[j + 1].price) {
                    MenuItem temp = menu[j];
                    menu[j] = menu[j + 1];
                    menu[j + 1] = temp;
                }
            }
        }
    }

    public static int linearSearch(MenuItem[] menu, MenuItem sample) {
        for (int i = 0; i < menu.length; i++) {
            if (menu[i].sameAs(sample)) {
                return i;
            }
        }
        return -1;
    }
}
