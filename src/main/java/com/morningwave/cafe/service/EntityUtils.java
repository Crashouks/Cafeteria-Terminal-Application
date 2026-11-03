package com.morningwave.cafe.service;

import com.morningwave.cafe.model.BaseEntity;
import java.util.Collection;

public final class EntityUtils {

    private EntityUtils() {
    }

    /**
     * {@code ? extends BaseEntity}: метод лише ЧИТАЄ елементи, тож приймає List&lt;Drink&gt;,
     * List&lt;MenuItem&gt; тощо. List&lt;Drink&gt; не є List&lt;BaseEntity&gt; (узагальнення інваріантні):
     * інакше через List&lt;BaseEntity&gt; можна було б додати Dessert у список напоїв.
     * Wildcard забороняє додавання, тож читання безпечне.
     */
    public static void printAll(Collection<? extends BaseEntity> items) {
        for (BaseEntity item : items) {
            System.out.println("  #" + item.getId() + " " + item);
        }
    }
}
