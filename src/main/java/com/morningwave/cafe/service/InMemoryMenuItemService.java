package com.morningwave.cafe.service;

import com.morningwave.cafe.model.BaseEntity;
import com.morningwave.cafe.model.MenuItem;
import java.util.Comparator;
import java.util.List;

public class InMemoryMenuItemService extends InMemoryCrudService<MenuItem> implements MenuItemService {

    /**
     * Comparator, а не Comparable: порядок «назва, потім ціна» — один із багатьох можливих,
     * тому його не варто вшивати в compareTo самої сутності (див. README).
     */
    private static final Comparator<MenuItem> BY_NAME_THEN_PRICE =
            Comparator.<MenuItem, String>comparing(BaseEntity::getName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparingDouble(MenuItem::getPrice);

    @Override
    public List<MenuItem> findAllSortedByNameAndByPrice() {
        return findAllSorted(BY_NAME_THEN_PRICE);
    }
}
