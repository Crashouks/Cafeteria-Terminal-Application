class NoDiscount implements DiscountStrategy {

    public double calculate(double price) {
        return price;
    }

    public String getName() {
        return "Без знижки";
    }
}

abstract class BaseDiscount implements DiscountStrategy, Describable {

    protected double notNegative(double value) {
        return value < 0 ? 0 : value;
    }

    public String describe() {
        return getName() + ": 100 грн -> " + calculate(100) + " грн";
    }
}

class PercentDiscount extends BaseDiscount {

    private double percent;

    public PercentDiscount(double percent) {
        this.percent = percent;
    }

    public double calculate(double price) {
        return notNegative(price - price * percent / 100);
    }

    public String getName() {
        return "Знижка " + percent + "%";
    }
}

class FixedDiscount extends BaseDiscount {

    private double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    public double calculate(double price) {
        return notNegative(price - amount);
    }

    public String getName() {
        return "Знижка " + amount + " грн";
    }
}

class PriceCalculator {

    private DiscountStrategy strategy;

    public PriceCalculator(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public String getStrategyName() {
        return strategy.getName();
    }

    public double getPrice(MenuItem item) {
        return strategy.calculate(item.price);
    }
}
