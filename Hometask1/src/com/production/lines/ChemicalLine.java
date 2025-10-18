package com.production.lines;

import com.production.products.ChemicalProduct;
import com.production.products.Product;

import java.util.List;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String l, List<ChemicalProduct> p, double e) {
        super(l, p, e);
    }

    @Override
    public boolean canProduce(Product product) {
        if (product instanceof ChemicalProduct) {
            return true;
        }
        return false;
    }
}


