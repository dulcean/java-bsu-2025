package factory.productionLines;

import factory.products.ChemicalProduct;
import factory.products.Product;

public class ChemicalsLine extends ProductionLine<ChemicalProduct> {
    public ChemicalsLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    boolean canProduce (Product product) {
        return product instanceof ChemicalProduct;
    }

    @Override
    public String getCategory() {
        return "Chemical";
    }
}
