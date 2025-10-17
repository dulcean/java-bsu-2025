package lab1.model.line;

import lab1.model.product.ChemicalProduct;
import lab1.model.product.Product;

public final class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
