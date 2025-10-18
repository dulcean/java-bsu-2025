package products;

import java.util.List;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String id, List<MechanicalProduct> items, double efficiency) {
        super(id, items, efficiency);
    }

    public MechanicalLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}