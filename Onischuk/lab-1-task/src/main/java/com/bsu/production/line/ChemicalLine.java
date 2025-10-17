package com.bsu.production.line;

import com.bsu.production.model.ChemicalProduct;
import com.bsu.production.model.Product;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
