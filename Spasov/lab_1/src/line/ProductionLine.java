package line;

import model.Product;
import util.validator.ProductionLineValidator;
import util.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private static final Validator<ProductionLine<?>> DEFAULT_VALIDATOR = new ProductionLineValidator();
    protected final List<T> products;
    private final String lineId;
    private final double efficiency;

    public ProductionLine(String lineId, double efficiency) {
        this(lineId, efficiency, DEFAULT_VALIDATOR);
    }

    public ProductionLine(String lineId, double efficiency, Validator<ProductionLine<?>> validator) {
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.products = new ArrayList<>();
        validator.validate(this);
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

    public void addProduct(Product product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException("Line with LineID " + lineId + " can not produce product with category " + product.getCategory());
        }
        T casted = (T) product;
        products.add(casted);
    }

    @Override
    public String toString() {
        return "Line " + lineId + " (" + getClass().getSimpleName() + "), efficiency=" + efficiency;
    }
}
