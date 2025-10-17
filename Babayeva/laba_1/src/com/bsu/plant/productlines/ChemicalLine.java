package com.bsu.plant.productlines;

import com.bsu.plant.product.ChemicalProduct;
import com.bsu.plant.product.Product;
import java.util.List;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    public ChemicalLine(String lineId, List<ChemicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
