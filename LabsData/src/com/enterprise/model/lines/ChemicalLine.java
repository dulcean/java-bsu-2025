package com.enterprise.model.lines;

import com.enterprise.model.products.ChemicalProduct;
import com.enterprise.model.products.Product;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
