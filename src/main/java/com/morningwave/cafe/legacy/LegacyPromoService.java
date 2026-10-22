package com.morningwave.cafe.legacy;

public class LegacyPromoService {

    public long promoCost(long kopecks, String promoCode) {
        if (promoCode.equals("MORNING")) {
            return kopecks * 85 / 100;
        }
        return kopecks;
    }
}
