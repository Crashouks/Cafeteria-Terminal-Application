import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("=== ЛР1: Облік товару ===");

        System.out.print("Введіть назву товару: ");
        String name = scanner.nextLine().trim();

        System.out.print("Введіть категорію товару: ");
        String category = scanner.nextLine().trim();

        System.out.print("Введіть кількість одиниць товару (шт): ");
        int quantity = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Введіть ціну за одиницю товару (грн): ");
        double price = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

        System.out.print("Введіть знижку на товар (%): ");
        double discountPercent = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

        double totalBeforeDiscount = quantity * price;
        double discountAmount = totalBeforeDiscount * discountPercent / 100.0;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;

        String discountStatus;
        if (discountPercent > 0) {
            discountStatus = "Знижку застосовано.";
        } else {
            discountStatus = "Знижка відсутня.";
        }

        String stockStatus;
        if (quantity > 10) {
            stockStatus = "Товару достатньо на складі.";
        } else if (quantity > 0) {
            stockStatus = "Товару обмаль на складі.";
        } else {
            stockStatus = "Товар відсутній на складі.";
        }

        System.out.println();
        System.out.println("=== Результат розрахунку ===");
        System.out.printf(Locale.US, "Товар:                %s%n", name);
        System.out.printf(Locale.US, "Категорія:             %s%n", category);
        System.out.printf(Locale.US, "Кількість:             %d шт.%n", quantity);
        System.out.printf(Locale.US, "Ціна за одиницю:       %.2f грн%n", price);
        System.out.printf(Locale.US, "Сума без знижки:       %.2f грн%n", totalBeforeDiscount);
        System.out.printf(Locale.US, "Знижка:                %.2f%% (%.2f грн)%n", discountPercent, discountAmount);
        System.out.printf(Locale.US, "Сума до сплати:        %.2f грн%n", totalAfterDiscount);
        System.out.println(discountStatus);
        System.out.println(stockStatus);

        scanner.close();
    }
}
