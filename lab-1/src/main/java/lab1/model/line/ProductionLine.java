package lab1.model.line;

import lab1.model.product.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    private final List<T> products = new ArrayList<>();
    private final double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        if (lineId == null || lineId.isBlank()) {
            throw new IllegalArgumentException("lineId is blank");
        }
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("efficiency must be in [0,1]");
        }

        this.lineId = lineId;
        this.efficiency = efficiency;
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public double getEfficiency() {
        return efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (product == null) {
            throw new IllegalArgumentException("product is null");
        }
        if (!canProduce(product)) {
            throw new IllegalArgumentException("Can't produce product for line " + lineId);
        }

        products.add(product);
    }
}
