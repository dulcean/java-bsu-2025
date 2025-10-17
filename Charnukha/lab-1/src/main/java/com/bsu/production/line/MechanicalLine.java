package com.bsu.production.line;

import com.bsu.production.model.MechanicalProduct;
import com.bsu.production.model.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
