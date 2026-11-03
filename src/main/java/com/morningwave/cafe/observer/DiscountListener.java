package com.morningwave.cafe.observer;

public interface DiscountListener {

    void onDiscountChanged(String oldName, String newName);
}
