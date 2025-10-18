package production.line;

import production.model.ElectronicProduct;
import production.model.Product;

public class ElectronicsLine extends ProductionLine<ElectronicProduct> {

    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}
