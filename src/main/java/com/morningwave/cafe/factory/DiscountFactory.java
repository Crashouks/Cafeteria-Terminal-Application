package com.morningwave.cafe.factory;

import com.morningwave.cafe.adapter.LegacyPromoAdapter;
import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.discount.FixedDiscount;
import com.morningwave.cafe.discount.NoDiscount;
import com.morningwave.cafe.discount.PercentDiscount;
import com.morningwave.cafe.legacy.LegacyPromoService;

public class DiscountFactory {

    public static DiscountStrategy create(String type, double value) {
        switch (type) {
            case "none":
                return new NoDiscount();
            case "percent":
                return new PercentDiscount(value);
            case "fixed":
                return new FixedDiscount(value);
            case "promo":
                return new LegacyPromoAdapter(new LegacyPromoService(), "MORNING");
            default:
                throw new IllegalArgumentException("Невідомий тип знижки: " + type);
        }
    }
}
