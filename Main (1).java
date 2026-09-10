import java.util.Locale;
import java.util.Scanner;

/**
 * ЛР1. Кав'ярня: приймання та облік замовлень.
 * Програма приймає ДВА замовлення за зміну (введення характеристик,
 * розрахунок вартості кожного), а потім формує ОБЛІК зміни:
 * загальну виручку, середній чек, найбільший чек і виконання плану.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); // крапка як десятковий роздільник для double

        System.out.println("=== Кав'ярня \"Ранкова Хвиля\" — приймання та облік замовлень ===");

        System.out.print("Дата зміни (напр., 10.09.2026): ");
        String shiftDate = scanner.nextLine();

        System.out.print("Плановий обсяг виручки за зміну, грн: ");
        double revenuePlan = scanner.nextDouble();
        scanner.nextLine(); // прибрати символ переносу рядка, що лишився після nextDouble()

        // ==================== ЗАМОВЛЕННЯ №1 ====================
        System.out.println();
        System.out.println("--- Замовлення №1 ---");

        System.out.print("Ім'я клієнта: ");
        String customerName1 = scanner.nextLine();

        System.out.print("Назва напою: ");
        String drinkName1 = scanner.nextLine();

        System.out.print("Об'єм порції, мл: ");
        int volumeMl1 = scanner.nextInt();

        System.out.print("Ціна за порцію, грн: ");
        double pricePerUnit1 = scanner.nextDouble();

        System.out.print("Кількість порцій: ");
        int quantity1 = scanner.nextInt();

        System.out.print("Знижка клієнта, %: ");
        double discountPercent1 = scanner.nextDouble();
        scanner.nextLine();

        String sizeLabel1;
        if (volumeMl1 >= 400) {
            sizeLabel1 = "Великий (L)";
        } else if (volumeMl1 >= 300) {
            sizeLabel1 = "Середній (M)";
        } else {
            sizeLabel1 = "Малий (S)";
        }

        double loyaltyBonus1;
        if (quantity1 >= 3) {
            loyaltyBonus1 = 5.0;
        } else {
            loyaltyBonus1 = 0.0;
        }

        double totalDiscount1 = discountPercent1 + loyaltyBonus1;
        if (totalDiscount1 > 100.0) {
            totalDiscount1 = 100.0;
        }

        double subtotal1 = pricePerUnit1 * quantity1;
        double discountAmount1 = subtotal1 * totalDiscount1 / 100.0;
        double total1 = subtotal1 - discountAmount1;

        // ==================== ЗАМОВЛЕННЯ №2 ====================
        System.out.println();
        System.out.println("--- Замовлення №2 ---");

        System.out.print("Ім'я клієнта: ");
        String customerName2 = scanner.nextLine();

        System.out.print("Назва напою: ");
        String drinkName2 = scanner.nextLine();

        System.out.print("Об'єм порції, мл: ");
        int volumeMl2 = scanner.nextInt();

        System.out.print("Ціна за порцію, грн: ");
        double pricePerUnit2 = scanner.nextDouble();

        System.out.print("Кількість порцій: ");
        int quantity2 = scanner.nextInt();

        System.out.print("Знижка клієнта, %: ");
        double discountPercent2 = scanner.nextDouble();
        scanner.nextLine();

        String sizeLabel2;
        if (volumeMl2 >= 400) {
            sizeLabel2 = "Великий (L)";
        } else if (volumeMl2 >= 300) {
            sizeLabel2 = "Середній (M)";
        } else {
            sizeLabel2 = "Малий (S)";
        }

        double loyaltyBonus2;
        if (quantity2 >= 3) {
            loyaltyBonus2 = 5.0;
        } else {
            loyaltyBonus2 = 0.0;
        }

        double totalDiscount2 = discountPercent2 + loyaltyBonus2;
        if (totalDiscount2 > 100.0) {
            totalDiscount2 = 100.0;
        }

        double subtotal2 = pricePerUnit2 * quantity2;
        double discountAmount2 = subtotal2 * totalDiscount2 / 100.0;
        double total2 = subtotal2 - discountAmount2;

        // ==================== ЧЕКИ ЗАМОВЛЕНЬ ====================
        System.out.println();
        System.out.println("================ ЧЕК №1 ================");
        System.out.printf("Клієнт:            %s%n", customerName1);
        System.out.printf("Напій:             %s (%s, %d мл)%n", drinkName1, sizeLabel1, volumeMl1);
        System.out.printf("Сума без знижки:   %.2f грн%n", subtotal1);
        System.out.printf("Знижка:            %.2f %% (%.2f грн)%n", totalDiscount1, discountAmount1);
        System.out.printf("До сплати:         %.2f грн%n", total1);
        System.out.println("==========================================");

        System.out.println();
        System.out.println("================ ЧЕК №2 ================");
        System.out.printf("Клієнт:            %s%n", customerName2);
        System.out.printf("Напій:             %s (%s, %d мл)%n", drinkName2, sizeLabel2, volumeMl2);
        System.out.printf("Сума без знижки:   %.2f грн%n", subtotal2);
        System.out.printf("Знижка:            %.2f %% (%.2f грн)%n", totalDiscount2, discountAmount2);
        System.out.printf("До сплати:         %.2f грн%n", total2);
        System.out.println("==========================================");

        // ==================== ОБЛІК ЗМІНИ ====================
        int orderCount = 2;
        double totalRevenue = total1 + total2;
        double averageCheck = totalRevenue / orderCount;

        double biggestCheck;
        String biggestCustomer;
        if (total1 >= total2) {
            biggestCheck = total1;
            biggestCustomer = customerName1;
        } else {
            biggestCheck = total2;
            biggestCustomer = customerName2;
        }

        double planDiff = totalRevenue - revenuePlan;
        String planStatus;
        if (planDiff >= 0) {
            planStatus = "ВИКОНАНО";
        } else {
            planStatus = "НЕ ВИКОНАНО";
        }

        System.out.println();
        System.out.println("============ ОБЛІК ЗМІНИ (" + shiftDate + ") ============");
        System.out.printf("Кількість замовлень:      %d%n", orderCount);
        System.out.printf("Загальна виручка:         %.2f грн%n", totalRevenue);
        System.out.printf("Середній чек:             %.2f грн%n", averageCheck);
        System.out.printf("Найбільший чек:           %.2f грн (клієнт: %s)%n", biggestCheck, biggestCustomer);
        System.out.printf("План на зміну:            %.2f грн%n", revenuePlan);
        System.out.printf("Відхилення від плану:     %.2f грн%n", planDiff);
        System.out.printf("Статус плану:             %s%n", planStatus);
        System.out.println("=======================================================");

        scanner.close();
    }
}
