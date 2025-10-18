package com.production.lines;

import com.production.products.Product;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    protected final String lineId;
    protected final List<T> products;
    protected double efficiency;

    public ProductionLine(String lineId, List<T> initialProducts, double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Efficiency must be between 0.0 and 1.0");
        }
        this.lineId = lineId;
        this.products = new ArrayList<>(initialProducts);
        this.efficiency = efficiency;
    }

    @SuppressWarnings("unchecked")
    public void addProduct(Product product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                    "Validation Error: Line '" + this.lineId + "' of type " + this.getClass().getSimpleName()
                            + " cannot produce product '" + product.name() + "' of category '" + product.getCategory() + "'."
            );
        }
        this.products.add((T) product);
        System.out.println("Success: Product '" + product.name() + "' was added to line '" + this.lineId + "'.");
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

    public abstract boolean canProduce(Product product);
};