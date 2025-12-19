package products;

import java.util.List;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String id, List<ChemicalProduct> items, double efficiency) {
        super(id, items, efficiency);
    }

    public ChemicalLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}