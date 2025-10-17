package com.bsu.production.line;

import com.bsu.production.model.Product;
import com.bsu.production.validation.Validatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> implements Validatable {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        if (lineId == null || lineId.isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be null or empty");
        }
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Efficiency must be between 0.0 and 1.0");
        }
        
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.products = new ArrayList<>();
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public double getEfficiency() {
        return efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        validate(product);
        products.add(product);
    }

    @Override
    public void validate(Product product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                String.format("Product %s (category: %s) is not compatible with production line %s", 
                    product.getName(), product.getCategory(), lineId));
        }
    }

    @Override
    public String toString() {
        return String.format("%s{lineId='%s', efficiency=%.2f, productsCount=%d}", 
            getClass().getSimpleName(), lineId, efficiency, products.size());
    }
}
