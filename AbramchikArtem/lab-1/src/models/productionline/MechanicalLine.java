package models.productionline;

import models.product.MechanicalProduct;
import models.product.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}