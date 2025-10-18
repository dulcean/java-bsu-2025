package com.production.products;

public interface Product {
    String id();
    String name();
    int productionTime();
    String getCategory();
/*
    Product(String id, String name, int productionTime) {
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
    }
*/
    default String getInfo() {
        return "Product{" +
            "id='" + id() + '\'' +
            ", name='" + name() + '\'' +
            ", category='" + getCategory() + '\'' +
            ", productionTime=" + productionTime() +
            '}';
}

};