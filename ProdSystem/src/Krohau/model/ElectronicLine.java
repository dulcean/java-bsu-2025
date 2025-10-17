package Krohau.model;

import java.util.List;

public class ElectronicLine extends ProductionLine<ElectronicProduct> {
    public ElectronicLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    public ElectronicLine(String lineId, List<ElectronicProduct> products, double efficiency) {
        super(lineId, products, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}