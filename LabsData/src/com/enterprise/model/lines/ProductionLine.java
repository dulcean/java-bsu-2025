package com.enterprise.model.lines;

import com.enterprise.model.products.Product;
import com.enterprise.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> implements Validator<Product> {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        this.lineId = lineId;
        this.products = new ArrayList<>();
        this.efficiency = efficiency;
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

    public void addProduct(T product) {
        validate(product);
        products.add(product);
    }

    public abstract boolean canProduce(Product product);

    @Override
    public void validate(Product product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                    "Product " + product.name() + " is not compatible with this production line."
            );
        }
    }
}
