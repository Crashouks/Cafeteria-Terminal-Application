import java.util.Locale;
import java.util.Scanner;

public class Main {

    static class MenuItem {
        String name;
        String category;
        double price;
        int calories;

        MenuItem(String name, String category, double price, int calories) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.calories = calories;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%-20s | %-15s | %8.2f грн | %4d ккал", name, category, price, calories);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Кафетерій \"Ранкова Хвиля\" — каталог меню ===");

        System.out.print("Введіть кількість позицій меню: ");
        int count = Integer.parseInt(scanner.nextLine().trim());

        MenuItem[] menu = new MenuItem[count];

        for (int i = 0; i < count; i++) {
            System.out.println();
            System.out.println("--- Позиція меню №" + (i + 1) + " ---");

            System.out.print("Назва страви/напою: ");
            String name = scanner.nextLine();

            System.out.print("Категорія: ");
            String category = scanner.nextLine();

            System.out.print("Ціна, грн: ");
            double price = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

            System.out.print("Калорійність, ккал: ");
            int calories = Integer.parseInt(scanner.nextLine().trim());

            menu[i] = new MenuItem(name, category, price, calories);
        }

        System.out.println();
        System.out.println("=== Меню (введені дані) ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        System.out.println();
        System.out.print("Введіть цінову межу для підрахунку дешевих позицій, грн: ");
        double priceLimit = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

        int cheapCount = 0;
        for (MenuItem item : menu) {
            if (item.price < priceLimit) {
                cheapCount++;
            }
        }
        System.out.printf(Locale.US, "Кількість позицій дешевших за %.2f грн: %d%n", priceLimit, cheapCount);

        System.out.println();
        System.out.println("=== Меню до сортування за ціною ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        for (int i = 0; i < menu.length - 1; i++) {
            for (int j = 0; j < menu.length - 1 - i; j++) {
                if (menu[j].price > menu[j + 1].price) {
                    MenuItem temp = menu[j];
                    menu[j] = menu[j + 1];
                    menu[j + 1] = temp;
                }
            }
        }

        System.out.println();
        System.out.println("=== Меню після сортування за ціною (зростання) ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        System.out.println();
        System.out.println("=== Пошук позиції меню за зразком ===");

        System.out.print("Назва страви/напою: ");
        String searchName = scanner.nextLine();

        System.out.print("Категорія: ");
        String searchCategory = scanner.nextLine();

        System.out.print("Ціна, грн: ");
        double searchPrice = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

        System.out.print("Калорійність, ккал: ");
        int searchCalories = Integer.parseInt(scanner.nextLine().trim());

        MenuItem sample = new MenuItem(searchName, searchCategory, searchPrice, searchCalories);
        int foundIndex = linearSearch(menu, sample);

        if (foundIndex >= 0) {
            System.out.println("Знайдено на позиції №" + (foundIndex + 1) + ": " + menu[foundIndex]);
        } else {
            System.out.println("Позицію з такими даними в меню не знайдено.");
        }

        scanner.close();
    }

    static int linearSearch(MenuItem[] menu, MenuItem sample) {
        for (int i = 0; i < menu.length; i++) {
            MenuItem current = menu[i];
            boolean same = current.name.equals(sample.name)
                    && current.category.equals(sample.category)
                    && current.price == sample.price
                    && current.calories == sample.calories;
            if (same) {
                return i;
            }
        }
        return -1;
    }
}
