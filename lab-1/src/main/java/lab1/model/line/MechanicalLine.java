package lab1.model.line;

import lab1.model.product.MechanicalProduct;
import lab1.model.product.Product;

public final class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
