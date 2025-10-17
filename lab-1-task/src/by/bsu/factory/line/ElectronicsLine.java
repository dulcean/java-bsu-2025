package by.bsu.factory.line;

import by.bsu.factory.product.ElectronicProduct;
import by.bsu.factory.product.Product;
import java.util.List;

public final class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, List<ElectronicProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
