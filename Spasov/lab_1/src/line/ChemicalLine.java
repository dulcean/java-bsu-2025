package line;

import model.ChemicalProduct;
import model.Product;

public class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
