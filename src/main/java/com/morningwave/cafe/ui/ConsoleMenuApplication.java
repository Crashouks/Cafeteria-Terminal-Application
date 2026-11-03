package com.morningwave.cafe.ui;

import com.morningwave.cafe.demo.CrudDemo;
import com.morningwave.cafe.demo.InheritanceDemo;
import com.morningwave.cafe.discount.Describable;
import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.discount.PriceCalculator;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.factory.DiscountFactory;
import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.observer.DiscountCounter;
import com.morningwave.cafe.observer.DiscountLogger;
import com.morningwave.cafe.service.MenuService;
import java.util.Scanner;

/** Coordinates the original interactive console workflow. */
public final class ConsoleMenuApplication {

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            run(new ConsoleInput(scanner));
        } finally {
            System.out.println("Роботу завершено.");
        }
    }

    private void run(ConsoleInput input) {
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
            if (item.getPrice() < limit) {
                cheap++;
            }
            total += item.getCalories();
        }
        System.out.println("Позицій дешевших за " + limit + " грн: " + cheap);

        if (menu.length == 0) {
            System.out.println("Меню порожнє, середню калорійність обчислити неможливо.");
        } else {
            System.out.println("Середня калорійність: " + total / menu.length + " ккал");
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
        if (number >= 1 && number <= menu.length) {
            System.out.println(menu[number - 1]);
        } else {
            System.out.println("Позиції з номером " + number + " немає в меню.");
        }

        demonstrateHierarchyExceptions();
        InheritanceDemo.run();
        CrudDemo.run();
        demonstrateDiscounts(input, menu);
    }

    private static void demonstrateHierarchyExceptions() {
        for (int i = 0; i < 2; i++) {
            try {
                if (i == 0) {
                    new Drink(0, "Тест", -1, 100);
                } else {
                    new Dessert(0, "Тест", 10, -5);
                }
            } catch (MenuException e) {
                System.out.println("Базовий catch: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            }
        }
    }

    private static void demonstrateDiscounts(ConsoleInput input, MenuItem[] menu) {
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
            if (strategy instanceof Describable describable) {
                System.out.println("  " + describable.describe());
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
                System.out.println(item.getName() + ": " + calculator.getPrice(item) + " грн");
            }
            for (int i = 0; i < strategies.length; i++) {
                System.out.println((i + 1) + " - " + strategies[i].getName());
            }
            choice = input.readInt("Оберіть знижку (0 - вихід): ");
            if (choice != 0) {
                if (choice < 1 || choice > strategies.length) {
                    System.out.println("Такої знижки немає.");
                } else {
                    calculator.setStrategy(strategies[choice - 1]);
                }
            }
        }
        System.out.println("Змін знижки за сеанс: " + counter.getCount());
    }
}
