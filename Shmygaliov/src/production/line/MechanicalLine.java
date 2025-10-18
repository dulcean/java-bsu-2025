package production.line;

import production.model.MechanicalProduct;
import production.model.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {

    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
