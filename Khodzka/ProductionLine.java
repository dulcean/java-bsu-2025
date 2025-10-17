package products;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineCode;
    private final List<T> items;
    private final double efficiency;

    protected ProductionLine(String lineCode, List<T> items, double efficiency) {
        this.lineCode = lineCode;
        this.items = items;
        this.efficiency = efficiency;
    }

    protected ProductionLine(String lineCode, double efficiency) {
        this.lineCode = lineCode;
        this.items = new ArrayList<>();
        this.efficiency = efficiency;
    }

    public String getLineCode() { return lineCode; }
    public List<T> getItems() { return new ArrayList<>(items); }
    public double getEfficiency() { return efficiency; }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (!canProduce(product)) throw new IllegalArgumentException("Неверный тип продукта");
        items.add(product);
    }
}