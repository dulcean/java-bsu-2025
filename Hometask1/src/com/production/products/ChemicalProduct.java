package com.production.products;

public record ChemicalProduct(String id, String name, int productionTime) implements Product {
    @Override
    public String getCategory() {
        return "Chemical";
    }
}