package com.morningwave.cafe;

import com.morningwave.cafe.discount.Describable;
import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.discount.PriceCalculator;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.factory.DiscountFactory;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.observer.DiscountCounter;
import com.morningwave.cafe.observer.DiscountLogger;
import com.morningwave.cafe.service.MenuService;
import com.morningwave.cafe.ui.ConsoleInput;
import java.util.Scanner;

public class Main {

    static void demoHierarchy() {
        for (int i = 0; i < 2; i++) {
            try {
                if (i == 0) {
                    new MenuItem("Тест", "Тест", -1, 100);
                } else {
                    new MenuItem("Тест", "Тест", 10, -5);
                }
            } catch (MenuException e) {
                System.out.println("Базовий catch: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            }
        }
    }

    static void demoDiscounts(ConsoleInput input, MenuItem[] menu) {
        DiscountStrategy[] strategies = {
                DiscountFactory.create("none", 0),
                DiscountFactory.create("percent", 10),
                DiscountFactory.create("fixed", 5),
                DiscountFactory.create("promo", 0)
        };

        System.out.println("=== Знижки (поліморфізм) ===");
        for (DiscountStrategy strategy : strategies) {
            System.out.println(strategy.getName() + ": 100 грн -> " + strategy.calculate(100)
                    + " грн, 3 порції -> " + strategy.calculateTotal(100, 3) + " грн");
            if (strategy instanceof Describable) {
                System.out.println("  " + ((Describable) strategy).describe());
            }
        }

        PriceCalculator calculator = new PriceCalculator(strategies[0]);
        DiscountCounter counter = new DiscountCounter();
        calculator.addListener(new DiscountLogger());
        calculator.addListener(counter);

        int choice = -1;
        while (choice != 0) {
            System.out.println("=== Ціни: " + calculator.getStrategyName() + " ===");
            for (MenuItem item : menu) {
                System.out.println(item.name + ": " + calculator.getPrice(item) + " грн");
            }
            for (int i = 0; i < strategies.length; i++) {
                System.out.println((i + 1) + " - " + strategies[i].getName());
            }
            choice = input.readInt("Оберіть знижку (0 - вихід): ");
            if (choice != 0) {
                try {
                    calculator.setStrategy(strategies[choice - 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Такої знижки немає.");
                }
            }
        }
        System.out.println("Змін знижки за сеанс: " + counter.getCount());
    }

    static void run(ConsoleInput input) {
        MenuItem[] menu = input.createMenu();

        for (int i = 0; i < menu.length; i++) {
            System.out.println("--- Позиція " + (i + 1) + " ---");
            menu[i] = input.inputItem();
        }

        MenuService.printMenu("=== Меню ===", menu);

        double limit = input.readDouble("Цінова межа: ");
        int cheap = 0;
        int total = 0;
        for (MenuItem item : menu) {
            if (item.price < limit) {
                cheap++;
            }
            total += item.calories;
        }
        System.out.println("Позицій дешевших за " + limit + " грн: " + cheap);

        try {
            System.out.println("Середня калорійність: " + total / menu.length + " ккал");
        } catch (ArithmeticException e) {
            System.out.println("Меню порожнє, середню калорійність обчислити неможливо.");
        }

        MenuService.printMenu("=== До сортування ===", menu);
        MenuService.sortByPrice(menu);
        MenuService.printMenu("=== Після сортування за ціною ===", menu);

        System.out.println("=== Пошук позиції ===");
        MenuItem sample = input.inputItem();
        int found = MenuService.linearSearch(menu, sample);
        if (found >= 0) {
            System.out.println("Знайдено на позиції " + (found + 1) + ": " + menu[found]);
        } else {
            System.out.println("Не знайдено.");
        }

        int number = input.readInt("Номер позиції для перегляду: ");
        try {
            System.out.println(menu[number - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Позиції з номером " + number + " немає в меню.");
        }

        System.out.println("=== Демонстрація ієрархії винятків ===");
        demoHierarchy();

        demoDiscounts(input, menu);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            run(new ConsoleInput(scanner));
        } finally {
            scanner.close();
            System.out.println("Роботу завершено.");
        }
    }
}
