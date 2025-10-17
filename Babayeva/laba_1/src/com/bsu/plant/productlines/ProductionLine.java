package com.bsu.plant.productlines;

import com.bsu.plant.product.Product;
import com.bsu.plant.validation.Validatable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> implements Validatable {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public double getEfficiency() {
        return efficiency;
    }

    protected ProductionLine(String lineId, List<T> products, double efficiency) {
        this.lineId = lineId;
        this.products = products;
        this.efficiency = efficiency;
    }

    protected ProductionLine(String lineId, double efficiency) {
        if (lineId == null || lineId.isEmpty() || efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Invailid index of efficiency");
        }
        
        this.lineId = lineId;
        this.products = new ArrayList<>();
        this.efficiency = efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        validate(product);
        products.add(product);
    }

    @Override
    public void validate(Product product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException("Null product");
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