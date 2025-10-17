package util.validator;

import model.Product;

public class ProductValidator implements Validator<Product> {
    @Override
    public void validate(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            throw new IllegalArgumentException("ID should not be blank");
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Name should not be blank");
        }
        if (product.getProductionTime() <= 0) {
            throw new IllegalArgumentException("Time should be a positive number");
        }
    }
}
