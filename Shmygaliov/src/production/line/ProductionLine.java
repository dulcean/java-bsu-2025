package production.line;

import production.model.Product;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    protected final List<T> products;
    private final double efficiency;

    public ProductionLine(String lineId, double efficiency) {
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.products = new ArrayList<>();
    }

    public String getLineId() {
        return lineId;
    }

    public List<T> getProducts() {
        return products;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException("Эта линия не может работать с данным типом продукта");
        }
        products.add(product);
    }
}
