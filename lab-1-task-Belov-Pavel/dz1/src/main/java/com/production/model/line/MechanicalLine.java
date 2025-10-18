package main.java.com.production.model.line;

import main.java.com.production.model.product.MechanicalProduct;
import main.java.com.production.model.product.Product;
import main.java.com.production.validation.ProductValidator;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {

    private static final ProductValidator MECHANICAL_VALIDATOR = product -> {
        if (product.getId() == null || product.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mechanical Product ID cannot be empty.");
        }
        return true;
    };

    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency, MECHANICAL_VALIDATOR);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
