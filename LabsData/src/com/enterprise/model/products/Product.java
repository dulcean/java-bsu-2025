package com.enterprise.model.products;

public abstract record Product(String id, String name, int productionTime) {
    public abstract String getCategory();
}
