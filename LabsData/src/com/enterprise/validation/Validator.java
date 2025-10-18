package com.enterprise.validation;

import com.enterprise.model.products.Product;

public interface Validator<T extends Product> {
    void validate(T product);
}
