package lab1.model.line;

import lab1.model.product.ElectronicProduct;
import lab1.model.product.Product;

public final class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
