package com.morningwave.cafe.discount;

import com.morningwave.cafe.model.MenuItem;
import com.morningwave.cafe.observer.DiscountListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PriceCalculator {

    private DiscountStrategy strategy;
    private final List<DiscountListener> listeners = new ArrayList<>();

    public PriceCalculator(DiscountStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public void addListener(DiscountListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    public void setStrategy(DiscountStrategy newStrategy) {
        Objects.requireNonNull(newStrategy, "newStrategy");
        if (newStrategy == strategy) {
            return;
        }
        String oldName = strategy.getName();
        strategy = newStrategy;
        for (DiscountListener listener : listeners) {
            listener.onDiscountChanged(oldName, newStrategy.getName());
        }
    }

    public String getStrategyName() {
        return strategy.getName();
    }

    public double getPrice(MenuItem item) {
        return strategy.calculate(item.getPrice());
    }
}
