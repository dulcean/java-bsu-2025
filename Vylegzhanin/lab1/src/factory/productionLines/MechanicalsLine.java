package factory.productionLines;

import factory.products.MechanicalProduct;
import factory.products.Product;

public class MechanicalsLine extends ProductionLine<MechanicalProduct> {
    public MechanicalsLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }

    @Override
    public String getCategory() {
        return "Mechanic";
    }

}
