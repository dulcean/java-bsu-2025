package com.bsu.production.model;

public class ElectronicProduct extends Product {
    
    public ElectronicProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    public String getCategory() {
        return "Electronics";
    }
}
