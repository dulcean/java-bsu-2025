package by.bsu.factory.line;

import by.bsu.factory.product.ChemicalProduct;
import by.bsu.factory.product.Product;
import java.util.List;

public final class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String lineId, List<ChemicalProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}
