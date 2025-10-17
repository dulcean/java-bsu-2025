package model.line;

import model.product.MechanicalProduct;
import model.product.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {

    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}