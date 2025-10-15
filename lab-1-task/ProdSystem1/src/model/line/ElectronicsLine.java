package model.line;

import model.product.ElectronicProduct;
import model.product.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {

    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}