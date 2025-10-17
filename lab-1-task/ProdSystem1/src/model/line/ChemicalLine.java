package model.line;

import model.product.ChemicalProduct;
import model.product.Product;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {

    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}