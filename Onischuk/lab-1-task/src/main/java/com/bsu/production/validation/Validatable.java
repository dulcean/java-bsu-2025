package com.bsu.production.validation;

import com.bsu.production.model.Product;

public interface Validatable {
    void validate(Product product) throws IllegalArgumentException;
}
