package com.morningwave.cafe.ui;

import com.morningwave.cafe.exception.InvalidPriceException;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.HotDrink;
import com.morningwave.cafe.model.MenuItem;
import java.util.Scanner;

public class ConsoleInput {

    private Scanner scanner;
    private long nextId = 1;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public double readDouble(String text) {
        while (true) {
            System.out.print(text);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести число. Спробуйте ще раз.");
            }
        }
    }

    public int readInt(String text) {
        while (true) {
            System.out.print(text);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести ціле число. Спробуйте ще раз.");
            }
        }
    }

    public MenuItem[] createMenu() {
        while (true) {
            System.out.print("Кількість позицій меню: ");
            try {
                return new MenuItem[Integer.parseInt(scanner.nextLine())];
            } catch (NumberFormatException e) {
                System.out.println("Помилка: кількість має бути цілим числом.");
            } catch (NegativeArraySizeException e) {
                System.out.println("Помилка: кількість не може бути від'ємною.");
            }
        }
    }

    public MenuItem inputItem() {
        MenuItem item = null;
        while (item == null) {
            try {
                item = readItemWithLog();
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести число. Спробуйте ще раз.");
            } catch (InvalidPriceException e) {
                System.out.println("Помилка: " + e.getMessage() + " (введено: " + e.value + "). Спробуйте ще раз.");
            } catch (MenuException e) {
                System.out.println("Помилка: " + e.getMessage() + ". Спробуйте ще раз.");
            }
        }
        return item;
    }

    private MenuItem readItemWithLog() throws MenuException {
        try {
            return readItem();
        } catch (InvalidPriceException e) {
            System.out.println("[ЛОГ] Некоректна ціна: " + e.value);
            throw e;
        }
    }

    private MenuItem readItem() throws MenuException {
        System.out.print("Тип (1 - напій, 2 - гарячий напій, 3 - десерт): ");
        int type = Integer.parseInt(scanner.nextLine());
        if (type < 1 || type > 3) {
            throw new MenuException("Невідомий тип позиції: " + type);
        }
        System.out.print("Назва: ");
        String name = scanner.nextLine();
        System.out.print("Ціна: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Калорійність: ");
        int calories = Integer.parseInt(scanner.nextLine());
        switch (type) {
            case 1:
                return new Drink(nextId++, name, price, calories, readIntField("Об'єм, мл: "));
            case 2:
                int volume = readIntField("Об'єм, мл: ");
                return new HotDrink(nextId++, name, price, calories, volume, readIntField("Температура, °C: "));
            default:
                return new Dessert(nextId++, name, price, calories, readIntField("Вага, г: "));
        }
    }

    private int readIntField(String text) {
        System.out.print(text);
        return Integer.parseInt(scanner.nextLine());
    }
}
