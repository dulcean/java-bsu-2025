package Krohau.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        this.lineId = lineId;
        this.products = new ArrayList<>();
        this.efficiency = efficiency;
    }

    protected ProductionLine(String lineId, List<T> products, double efficiency) {
        this(lineId, efficiency);
        if (products != null) {
            for (T product : products) {
                addProduct(product);
            }
        }
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return new ArrayList<>(products);
    }

    public double getEfficiency() {
        return efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        products.add(product);
    }

    @Override
    public String toString() {
        return "ProductionLine{" +
                "lineId='" + lineId + '\'' +
                ", efficiency=" + efficiency +
                ", productsCount=" + products.size() +
                '}';
    }
}
