package main.java.com.production.model.line;

import main.java.com.production.model.product.ElectronicProduct;
import main.java.com.production.model.product.Product;
import main.java.com.production.validation.ProductValidator;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {

    private static final ProductValidator ELECTRONICS_VALIDATOR = product -> {
        if (product.getProductionTimeMinutes() > 300) {
            throw new IllegalArgumentException("Electronic Product production time cannot exceed 300 minutes.");
        }
        return true;
    };

    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency, ELECTRONICS_VALIDATOR);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
