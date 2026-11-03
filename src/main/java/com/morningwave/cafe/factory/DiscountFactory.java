package com.morningwave.cafe.factory;

import com.morningwave.cafe.adapter.LegacyPromoAdapter;
import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.discount.FixedDiscount;
import com.morningwave.cafe.discount.NoDiscount;
import com.morningwave.cafe.discount.PercentDiscount;
import com.morningwave.cafe.legacy.LegacyPromoService;

public final class DiscountFactory {

    private DiscountFactory() {
    }

    public static DiscountStrategy create(String type, double value) {
        return switch (type) {
            case "none" -> new NoDiscount();
            case "percent" -> new PercentDiscount(value);
            case "fixed" -> new FixedDiscount(value);
            case "promo" -> new LegacyPromoAdapter(new LegacyPromoService(), "MORNING");
            default -> throw new IllegalArgumentException("Невідомий тип знижки: " + type);
        };
    }
}
