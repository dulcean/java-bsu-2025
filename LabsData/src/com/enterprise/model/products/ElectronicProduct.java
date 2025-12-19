package com.enterprise.model.products;

public class ElectronicProduct extends Product {
    public ElectronicProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}
