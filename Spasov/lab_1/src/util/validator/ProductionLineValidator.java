package util.validator;

import line.ProductionLine;

public class ProductionLineValidator implements Validator<ProductionLine<?>> {
    @Override
    public void validate(ProductionLine<?> line) {
        if (line.getLineId() == null || line.getLineId().isEmpty()) {
            throw new IllegalArgumentException("LineID should not be blank");
        }
        if (line.getEfficiency() < 0.0 || line.getEfficiency() > 1.0) {
            throw new IllegalArgumentException("Efficiency should be in range [0.0, 1.0]");
        }
    }
}
