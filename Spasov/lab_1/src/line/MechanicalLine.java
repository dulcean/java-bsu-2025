package line;

import model.MechanicalProduct;
import model.Product;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
