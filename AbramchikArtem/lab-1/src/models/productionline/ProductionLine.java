package models.productionline;

import models.product.Product;

import java.util.ArrayList;
import java.util.List;


public abstract class ProductionLine<T extends Product> {
    private String lineId;
    private List<T> products;
    private double efficiency;

    public ProductionLine(String lineId, double efficiency) {
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.products = new ArrayList<>();
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return products;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public boolean addProduct(T product) {
        if (canProduce(product)) {
            products.add(product);
            return true;
        }
        return false;
    }

    public abstract boolean canProduce(Product product);

    @Override
    public String toString() {
        return "ProductionLine{" +
                "lineId='" + lineId + '\'' +
                ", efficiency=" + efficiency +
                ", productsCount=" + products.size() +
                ", products=" + products +
                '}';
    }
}