package Krohau.model;

import java.util.List;

public class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    public MechanicalLine(String lineId, List<MechanicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}
