package com.morningwave.cafe.adapter;

import com.morningwave.cafe.discount.DiscountStrategy;
import com.morningwave.cafe.legacy.LegacyPromoService;

public class LegacyPromoAdapter implements DiscountStrategy {

    private LegacyPromoService service;
    private String promoCode;

    public LegacyPromoAdapter(LegacyPromoService service, String promoCode) {
        this.service = service;
        this.promoCode = promoCode;
    }

    public double calculate(double price) {
        long kopecks = Math.round(price * 100);
        return service.promoCost(kopecks, promoCode) / 100.0;
    }

    public String getName() {
        return "Промокод " + promoCode;
    }
}
