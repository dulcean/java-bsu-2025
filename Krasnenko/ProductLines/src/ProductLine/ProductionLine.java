package ProductLine;

import Product.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    protected final List<T> products = new ArrayList<>();
    private final double efficiency; // 0.0 - 1.0

    protected ProductionLine(String lineId, double efficiency) {
        this.lineId = lineId;
        if (efficiency < 0.0 || efficiency > 1.0)
            throw new IllegalArgumentException("Efficiency must be between 0 and 1");
        this.efficiency = efficiency;
    }

    public String getLineId() { return lineId; }
    public List<T> getProducts() { return Collections.unmodifiableList(products); }
    public double getEfficiency() { return efficiency; }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (!canProduce(product))
            throw new IllegalArgumentException("Incompatible product type for this line!");
        products.add(product);
    }
}

