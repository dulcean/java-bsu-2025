package com.bsu.plant.product;

public class ElectronicProduct extends Product {
    
    public ElectronicProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}