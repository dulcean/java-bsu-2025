package com.enterprise.model.lines;

import com.enterprise.model.products.MechanicalProduct;
import com.enterprise.model.products.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
