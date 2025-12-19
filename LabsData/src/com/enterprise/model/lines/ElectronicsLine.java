package com.enterprise.model.lines;

import com.enterprise.model.products.ElectronicProduct;
import com.enterprise.model.products.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
