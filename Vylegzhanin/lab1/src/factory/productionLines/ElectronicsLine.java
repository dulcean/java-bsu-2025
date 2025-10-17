package factory.productionLines;

import factory.products.ElectronicProduct;
import factory.products.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }

    @Override
    public String getCategory() {
        return "Electronic";
    }
}
