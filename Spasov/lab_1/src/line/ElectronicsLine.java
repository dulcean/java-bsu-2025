package line;

import model.ElectronicProduct;
import model.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
