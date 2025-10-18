package main.java.com.production.validation;

import main.java.com.production.model.product.Product;

@FunctionalInterface
public interface ProductValidator {
    boolean validate(Product product) throws IllegalArgumentException;
}