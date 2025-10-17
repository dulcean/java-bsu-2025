package model;

import util.validator.ProductValidator;
import util.validator.Validator;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    private static final Validator<Product> DEFAULT_VALIDATOR = new ProductValidator();

    public Product(String id, String name, int productionTime) {
        this(id, name, productionTime, DEFAULT_VALIDATOR);
    }

    public Product(String id, String name, int productionTime, Validator<Product> validator) {
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
        validator.validate(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public abstract String getCategory();

    @Override
    public String toString() {
        return name + " (" + getCategory() + ")";
    }
}
