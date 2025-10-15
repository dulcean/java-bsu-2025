package by.bsu.factory.line;

import by.bsu.factory.product.MechanicalProduct;
import by.bsu.factory.product.Product;
import java.util.List;

public final class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, List<MechanicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
