package com.bsu.production.model;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    public Product(String id, String name, int productionTime) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (productionTime < 0) {
            throw new IllegalArgumentException("Production time cannot be negative");
        }
        
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
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
        return String.format("%s{id='%s', name='%s', productionTime=%d, category='%s'}", 
            getClass().getSimpleName(), id, name, productionTime, getCategory());
    }
}
