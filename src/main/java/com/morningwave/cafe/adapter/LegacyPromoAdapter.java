package com.morningwave.cafe.adapter;

import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.legacy.LegacyPromoService;
import java.util.Objects;

public final class LegacyPromoAdapter implements DiscountStrategy {

    private final LegacyPromoService service;
    private final String promoCode;

    public LegacyPromoAdapter(LegacyPromoService service, String promoCode) {
        this.service = Objects.requireNonNull(service, "service");
        this.promoCode = Objects.requireNonNull(promoCode, "promoCode");
    }

    @Override
    public double calculate(double price) {
        long kopecks = Math.round(price * 100);
        return service.promoCost(kopecks, promoCode) / 100.0;
    }

    @Override
    public String getName() {
        return "Промокод " + promoCode;
    }
}
