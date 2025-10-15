package model.line;

import model.product.Product;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {

    private final String lineId;
    private final List<T> products;
    private final double efficiency;

    public ProductionLine(String lineId, double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new IllegalArgumentException("Efficiency must be between 0.0 and 1.0.");
        }
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.products = new ArrayList<>();
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                    "Line " + lineId + " cannot produce product of type " + product.getCategory()
            );
        }
        products.add(product);
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return new ArrayList<>(products);
    }

    public double getEfficiency() {
        return efficiency;
    }
}