package com.morningwave.cafe.observer;

public class DiscountLogger implements DiscountListener {

    public void onDiscountChanged(String oldName, String newName) {
        System.out.println("[ЛОГ] Знижку змінено: " + oldName + " -> " + newName);
    }
}
