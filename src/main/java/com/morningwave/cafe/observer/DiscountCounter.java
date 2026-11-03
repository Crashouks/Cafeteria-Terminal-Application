package com.morningwave.cafe.observer;

public class DiscountCounter implements DiscountListener {

    private int count = 0;

    public void onDiscountChanged(String oldName, String newName) {
        count++;
    }

    public int getCount() {
        return count;
    }
}
