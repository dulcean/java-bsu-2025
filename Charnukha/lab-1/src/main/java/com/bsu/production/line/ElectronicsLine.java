package com.bsu.production.line;

import com.bsu.production.model.ElectronicProduct;
import com.bsu.production.model.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
