package com.morningwave.cafe.service;

import com.morningwave.cafe.model.MenuItem;
import java.util.List;

/**
 * Сервіс позицій меню. Метод сортування «назва → ціна» залежить від поля price, якого немає в
 * BaseEntity, тому він живе тут, а не в загальному CRUDService: не кожна сутність має ціну.
 */
public interface MenuItemService extends CRUDService<MenuItem> {

    /** Спочатку за назвою, при збігу назв — за ціною (за зростанням). Повертає нову колекцію. */
    List<MenuItem> findAllSortedByNameAndByPrice();
}
