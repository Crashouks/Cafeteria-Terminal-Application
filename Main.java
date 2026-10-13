import java.util.Scanner;

public class Main {

    static class MenuException extends Exception {
        MenuException(String message) {
            super(message);
        }
    }

    static class InvalidPriceException extends MenuException {
        double value;

        InvalidPriceException(String message, double value) {
            super(message);
            this.value = value;
        }
    }

    static class InvalidCaloriesException extends MenuException {
        int value;

        InvalidCaloriesException(String message, int value) {
            super(message);
            this.value = value;
        }
    }

    static class MenuItem {
        String name;
        String category;
        double price;
        int calories;

        MenuItem(String name, String category, double price, int calories) throws MenuException {
            if (price <= 0) {
                throw new InvalidPriceException("Ціна має бути більшою за 0", price);
            }
            if (calories < 0) {
                throw new InvalidCaloriesException("Калорійність не може бути від'ємною", calories);
            }
            this.name = name;
            this.category = category;
            this.price = price;
            this.calories = calories;
        }

        @Override
        public String toString() {
            return name + " | " + category + " | " + price + " грн | " + calories + " ккал";
        }
    }

    static MenuItem readItem(Scanner scanner) throws MenuException {
        System.out.print("Назва: ");
        String name = scanner.nextLine();
        System.out.print("Категорія: ");
        String category = scanner.nextLine();
        System.out.print("Ціна: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Калорійність: ");
        int calories = Integer.parseInt(scanner.nextLine());
        return new MenuItem(name, category, price, calories);
    }

    static MenuItem readItemWithLog(Scanner scanner) throws MenuException {
        try {
            return readItem(scanner);
        } catch (InvalidPriceException e) {
            System.out.println("[ЛОГ] Некоректна ціна: " + e.value);
            throw e;
        }
    }

    static MenuItem inputItem(Scanner scanner) {
        MenuItem item = null;
        while (item == null) {
            try {
                item = readItemWithLog(scanner);
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

    static double readDouble(Scanner scanner, String text) {
        while (true) {
            System.out.print(text);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести число. Спробуйте ще раз.");
            }
        }
    }

    static int readInt(Scanner scanner, String text) {
        while (true) {
            System.out.print(text);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно ввести ціле число. Спробуйте ще раз.");
            }
        }
    }

    static int linearSearch(MenuItem[] menu, MenuItem sample) {
        for (int i = 0; i < menu.length; i++) {
            if (menu[i].name.equals(sample.name)
                    && menu[i].category.equals(sample.category)
                    && menu[i].price == sample.price
                    && menu[i].calories == sample.calories) {
                return i;
            }
        }
        return -1;
    }

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

    static void printMenu(String title, MenuItem[] menu) {
        System.out.println(title);
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    static void run(Scanner scanner) {
        MenuItem[] menu = null;
        while (menu == null) {
            System.out.print("Кількість позицій меню: ");
            try {
                int count = Integer.parseInt(scanner.nextLine());
                menu = new MenuItem[count];
            } catch (NumberFormatException e) {
                System.out.println("Помилка: кількість має бути цілим числом.");
            } catch (NegativeArraySizeException e) {
                System.out.println("Помилка: кількість не може бути від'ємною.");
            }
        }

        for (int i = 0; i < menu.length; i++) {
            System.out.println("--- Позиція " + (i + 1) + " ---");
            menu[i] = inputItem(scanner);
        }

        printMenu("=== Меню ===", menu);

        double limit = readDouble(scanner, "Цінова межа: ");
        int cheap = 0;
        for (MenuItem item : menu) {
            if (item.price < limit) {
                cheap++;
            }
        }
        System.out.println("Позицій дешевших за " + limit + " грн: " + cheap);

        int total = 0;
        for (MenuItem item : menu) {
            total += item.calories;
        }
        try {
            int average = total / menu.length;
            System.out.println("Середня калорійність: " + average + " ккал");
        } catch (ArithmeticException e) {
            System.out.println("Меню порожнє, середню калорійність обчислити неможливо.");
        }

        printMenu("=== До сортування ===", menu);

        for (int i = 0; i < menu.length - 1; i++) {
            for (int j = 0; j < menu.length - 1 - i; j++) {
                if (menu[j].price > menu[j + 1].price) {
                    MenuItem temp = menu[j];
                    menu[j] = menu[j + 1];
                    menu[j + 1] = temp;
                }
            }
        }

        printMenu("=== Після сортування за ціною ===", menu);

        System.out.println("=== Пошук позиції ===");
        MenuItem sample = inputItem(scanner);
        int found = linearSearch(menu, sample);
        if (found >= 0) {
            System.out.println("Знайдено на позиції " + (found + 1) + ": " + menu[found]);
        } else {
            System.out.println("Не знайдено.");
        }

        int number = readInt(scanner, "Номер позиції для перегляду: ");
        try {
            System.out.println(menu[number - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Позиції з номером " + number + " немає в меню.");
        }

        System.out.println("=== Демонстрація ієрархії винятків ===");
        demoHierarchy();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            run(scanner);
        } finally {
            scanner.close();
            System.out.println("Роботу завершено.");
        }
    }
}