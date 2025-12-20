package com.bsu.plant.productlines;

import com.bsu.plant.product.ElectronicProduct;
import com.bsu.plant.product.Product;
import java.util.List;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    public ElectronicsLine(String lineId, List<ElectronicProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
