package com.bsu.plant.validation;

import com.bsu.plant.product.Product;

public interface Validatable {
    void validate(Product product) throws IllegalArgumentException;
}