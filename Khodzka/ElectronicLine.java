package products;

import java.util.List;

public class ElectronicLine extends ProductionLine<ElectronicProduct> {
    public ElectronicLine(String id, List<ElectronicProduct> items, double efficiency) {
        super(id, items, efficiency);
    }

    public ElectronicLine(String id, double efficiency) {
        super(id, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}