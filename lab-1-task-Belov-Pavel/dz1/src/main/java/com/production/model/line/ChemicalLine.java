package main.java.com.production.model.line;

import main.java.com.production.model.product.ChemicalProduct;
import main.java.com.production.model.product.Product;
import main.java.com.production.validation.ProductValidator;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {

    private static final ProductValidator CHEMICAL_VALIDATOR = product -> true;

    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency, CHEMICAL_VALIDATOR);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}