package com.production.lines;

import com.production.products.MechanicalProduct;
import com.production.products.Product;

import java.util.List;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String l, List<MechanicalProduct> p, double e) {
        super(l, p, e);
    }

    @Override
    public boolean canProduce(Product product) {
        if (product instanceof MechanicalProduct) {
            return true;
        }
        return false;
    }
}
