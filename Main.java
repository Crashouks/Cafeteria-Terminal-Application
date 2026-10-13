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
            return name + " | " + category + " | " + price + " грн | " + calories + " ккал";
        }
    }

    static MenuItem readItem(Scanner scanner) {
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

    // Лінійний пошук за цілим об'єктом-зразком
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Кількість позицій меню: ");
        int count = Integer.parseInt(scanner.nextLine());

        MenuItem[] menu = new MenuItem[count];
        for (int i = 0; i < menu.length; i++) {
            System.out.println("--- Позиція " + (i + 1) + " ---");
            menu[i] = readItem(scanner);
        }

        System.out.println("=== Меню ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        // Підрахунок елементів за умовою
        System.out.print("Цінова межа: ");
        double limit = Double.parseDouble(scanner.nextLine());
        int cheap = 0;
        for (MenuItem item : menu) {
            if (item.price < limit) {
                cheap++;
            }
        }
        System.out.println("Позицій дешевших за " + limit + " грн: " + cheap);

        // Сортування бульбашкою за ціною
        System.out.println("=== До сортування ===");
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

        System.out.println("=== Після сортування за ціною ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        // Пошук за зразком
        System.out.println("=== Пошук позиції ===");
        MenuItem sample = readItem(scanner);
        int found = linearSearch(menu, sample);
        if (found >= 0) {
            System.out.println("Знайдено на позиції " + (found + 1) + ": " + menu[found]);
        } else {
            System.out.println("Не знайдено.");
        }
    }
}
