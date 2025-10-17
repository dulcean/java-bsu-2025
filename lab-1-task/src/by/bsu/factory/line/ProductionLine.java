package by.bsu.factory.line;

import by.bsu.factory.product.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    protected ProductionLine(String lineId, List<T> products, double efficiency) {
        if (lineId == null || lineId.isBlank()) throw new IllegalArgumentException("lineId");
        if (products == null) throw new IllegalArgumentException("products");
        if (efficiency < 0.0 || efficiency > 1.0) throw new IllegalArgumentException("efficiency");
        this.lineId = lineId;
        this.products = new ArrayList<>(products);
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
        if (!canProduce(product)) throw new IllegalArgumentException("incompatible product");
        products.add(product);
    }
}