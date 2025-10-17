package com.bsu.production.line;

import com.bsu.production.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    protected final List<T> products = new ArrayList<>();
    private double efficiency;

    public ProductionLine(String lineId, double efficiency) {
        this.lineId = Objects.requireNonNull(lineId);
        setEfficiency(efficiency);
    }

    public String getLineId() { return lineId; }

    public List<T> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public double getEfficiency() { return efficiency; }

    public void setEfficiency(double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("efficiency must be between 0.0 and 1.0");
        }
        this.efficiency = efficiency;
    }

    public void addProduct(T product) {
        products.add(Objects.requireNonNull(product));
    }

    public boolean removeProduct(T product) {
        return products.remove(product);
    }

    public abstract boolean canProduce(Product product);
}
