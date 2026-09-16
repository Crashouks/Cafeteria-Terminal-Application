import java.util.Locale;
import java.util.Scanner;

public class Main {

    static class Order {
        String customerName;
        String drinkName;
        int volumeMl;
        String sizeLabel;
        double subtotal;
        double totalDiscount;
        double discountAmount;
        double total;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Кав'ярня \"Ранкова Хвиля\" — приймання та облік замовлень ===");

        System.out.print("Дата зміни (напр., 10.09.2026): ");
        String shiftDate = scanner.nextLine();

        System.out.print("Плановий обсяг виручки за зміну, грн: ");
        double revenuePlan = scanner.nextDouble();
        scanner.nextLine();

        Order order1 = readOrder(scanner, 1);
        Order order2 = readOrder(scanner, 2);

        printReceipt(1, order1);
        printReceipt(2, order2);

        int orderCount = 2;
        double totalRevenue = order1.total + order2.total;
        double averageCheck = totalRevenue / orderCount;
        Order biggest = order1.total >= order2.total ? order1 : order2;
        double planDiff = totalRevenue - revenuePlan;
        String planStatus = planDiff >= 0 ? "ВИКОНАНО" : "НЕ ВИКОНАНО";

        System.out.println();
        System.out.println("============ ОБЛІК ЗМІНИ (" + shiftDate + ") ============");
        System.out.printf("Кількість замовлень:      %d%n", orderCount);
        System.out.printf("Загальна виручка:         %.2f грн%n", totalRevenue);
        System.out.printf("Середній чек:             %.2f грн%n", averageCheck);
        System.out.printf("Найбільший чек:           %.2f грн (клієнт: %s)%n", biggest.total, biggest.customerName);
        System.out.printf("План на зміну:            %.2f грн%n", revenuePlan);
        System.out.printf("Відхилення від плану:     %.2f грн%n", planDiff);
        System.out.printf("Статус плану:             %s%n", planStatus);
        System.out.println("=======================================================");

        scanner.close();
    }

    static Order readOrder(Scanner scanner, int index) {
        System.out.println();
        System.out.println("--- Замовлення №" + index + " ---");

        System.out.print("Ім'я клієнта: ");
        String customerName = scanner.nextLine();

        System.out.print("Назва напою: ");
        String drinkName = scanner.nextLine();

        System.out.print("Об'єм порції, мл: ");
        int volumeMl = scanner.nextInt();

        System.out.print("Ціна за порцію, грн: ");
        double pricePerUnit = scanner.nextDouble();

        System.out.print("Кількість порцій: ");
        int quantity = scanner.nextInt();

        System.out.print("Знижка клієнта, %: ");
        double discountPercent = scanner.nextDouble();
        scanner.nextLine();

        Order order = new Order();
        order.customerName = customerName;
        order.drinkName = drinkName;
        order.volumeMl = volumeMl;
        order.sizeLabel = volumeMl >= 400 ? "Великий (L)" : volumeMl >= 300 ? "Середній (M)" : "Малий (S)";

        double loyaltyBonus = quantity >= 3 ? 5.0 : 0.0;
        order.totalDiscount = Math.min(discountPercent + loyaltyBonus, 100.0);
        order.subtotal = pricePerUnit * quantity;
        order.discountAmount = order.subtotal * order.totalDiscount / 100.0;
        order.total = order.subtotal - order.discountAmount;

        return order;
    }

    static void printReceipt(int index, Order order) {
        System.out.println();
        System.out.println("================ ЧЕК №" + index + " ================");
        System.out.printf("Клієнт:            %s%n", order.customerName);
        System.out.printf("Напій:             %s (%s, %d мл)%n", order.drinkName, order.sizeLabel, order.volumeMl);
        System.out.printf("Сума без знижки:   %.2f грн%n", order.subtotal);
        System.out.printf("Знижка:            %.2f %% (%.2f грн)%n", order.totalDiscount, order.discountAmount);
        System.out.printf("До сплати:         %.2f грн%n", order.total);
        System.out.println("==========================================");
    }
}