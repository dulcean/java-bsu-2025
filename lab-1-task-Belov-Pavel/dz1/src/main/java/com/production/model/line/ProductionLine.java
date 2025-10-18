package main.java.com.production.model.line;

import main.java.com.production.model.product.Product;
import main.java.com.production.validation.ProductValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;
    private final ProductValidator validator;

    protected ProductionLine(String lineId, double efficiency, ProductValidator validator) {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Efficiency must be between 0.0 and 1.0");
        }
        this.lineId = lineId;
        this.products = new ArrayList<>();
        this.efficiency = efficiency;
        this.validator = validator;
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

    public void addProduct(Product product) throws IllegalArgumentException {
        if (!canProduce(product)) {
            throw new IllegalArgumentException("Product of type " + product.getCategory() +
                    " is incompatible with line " + this.lineId);
        }

        validator.validate(product);

        T castedProduct = (T) product;
        this.products.add(castedProduct);
    }
}
