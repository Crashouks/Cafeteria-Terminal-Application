package com.morningwave.cafe.demo;

import com.morningwave.cafe.exception.EntityNotFoundException;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.model.BaseEntity;
import com.morningwave.cafe.model.Dessert;
import com.morningwave.cafe.model.Drink;
import com.morningwave.cafe.model.HotDrink;
import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.service.CRUDService;
import com.morningwave.cafe.service.EntityUtils;
import com.morningwave.cafe.service.InMemoryCrudService;
import com.morningwave.cafe.service.InMemoryMenuItemService;
import com.morningwave.cafe.service.MenuItemService;
import java.util.Comparator;

/** Демонстрація ЛР7–9: типізований CRUD-сервіс, колекції, сортування. */
public class CrudDemo {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println("=== ЛР7–9: CRUD-сервіс ===");
        try {
            demoCrud();
            demoSorting();
            demoWildcards();
        } catch (MenuException e) {
            System.out.println("Помилка демонстрації: " + e.getMessage());
        }
    }

    /** Той самий InMemoryCrudService без змін працює з двома сутностями. */
    private static void demoCrud() throws MenuException {
        System.out.println("--- CRUD: InMemoryCrudService<Drink> ---");
        CRUDService<Drink> drinks = new InMemoryCrudService<>();
        drinks.save(new Drink(1, "Лимонад", 40, 90, 300));
        drinks.save(new HotDrink(2, "Американо", 45, 5, 250, 85));
        System.out.println("Після додавання двох: " + drinks.findAll().size() + " запис(и)");

        drinks.save(new Drink(1, "Лимонад", 50, 90, 300));
        System.out.println("Оновлення id=1 (ціна 50): записів " + drinks.findAll().size()
                + ", ціна тепер " + drinks.findById(1).getPrice());

        System.out.println("delete(2) = " + drinks.delete(2));
        System.out.println("delete(2) ще раз = " + drinks.delete(2));
        try {
            drinks.findById(99);
        } catch (EntityNotFoundException e) {
            System.out.println("findById(99): " + e.getMessage() + " [id=" + e.getId() + "]");
        }

        System.out.println("--- CRUD: InMemoryCrudService<Dessert> (той самий клас) ---");
        CRUDService<Dessert> desserts = new InMemoryCrudService<>();
        desserts.save(new Dessert(1, "Чізкейк", 85, 380, 150));
        desserts.save(new Dessert(1, "Чізкейк", 90, 380, 150));
        System.out.println("Після двох save з id=1: записів " + desserts.findAll().size()
                + ", ціна " + desserts.findById(1).getPrice());
    }

    private static void demoSorting() throws MenuException {
        System.out.println("--- Сортування: MenuItemService ---");
        MenuItemService menu = new InMemoryMenuItemService();
        menu.save(new Drink(1, "Чай", 45, 2, 250));
        menu.save(new Dessert(2, "Чізкейк", 85, 380, 150));
        menu.save(new HotDrink(3, "Чай", 30, 2, 250, 80));
        menu.save(new Drink(4, "Американо", 40, 5, 200));

        System.out.println("До сортування (порядок додавання):");
        EntityUtils.printAll(menu.findAll());

        System.out.println("findAllSortedByName():");
        EntityUtils.printAll(menu.findAllSortedByName());

        System.out.println("findAllSortedByNameAndByPrice() (два «Чай» різняться ціною):");
        EntityUtils.printAll(menu.findAllSortedByNameAndByPrice());

        System.out.println("Після сортування сховище не змінилося:");
        EntityUtils.printAll(menu.findAll());
    }

    private static void demoWildcards() throws MenuException {
        System.out.println("--- Wildcards ---");
        InMemoryCrudService<Drink> drinks = new InMemoryCrudService<>();
        drinks.save(new Drink(1, "Сік", 35, 80, 200));
        drinks.save(new Drink(2, "Вода", 15, 0, 500));

        // Comparator<BaseEntity> і Comparator<MenuItem> підходять для T = Drink завдяки "? super T"
        Comparator<MenuItem> byPrice = Comparator.comparingDouble(MenuItem::getPrice);
        System.out.println("Drink-сервіс + Comparator<MenuItem> за ціною:");
        EntityUtils.printAll(drinks.findAllSorted(byPrice));
        System.out.println("Drink-сервіс + Comparator<BaseEntity> за назвою:");
        EntityUtils.printAll(drinks.findAllSorted(BaseEntity.BY_NAME));
    }
}
