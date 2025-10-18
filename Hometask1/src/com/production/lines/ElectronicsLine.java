package com.production.lines;

import com.production.products.ElectronicProduct;
import com.production.products.Product;

import java.util.List;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String l, List<ElectronicProduct> p, double e) {
        super(l, p, e);
    }

    @Override
    public boolean canProduce(Product product) {
        if (product instanceof ElectronicProduct) {
            return true;
        }
        return false;
    }
}
