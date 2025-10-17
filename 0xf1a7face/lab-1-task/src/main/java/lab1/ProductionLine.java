package lab1;

import java.util.List;

public abstract class ProductionLine<T extends Product> {
  protected String lineId;
  protected List<T> products;
  protected double efficiency;

  protected ProductionLine(String lineId, List<T> products, double efficiency) {
    if (!products.stream().allMatch(p -> p instanceof T)) {
      throw new IllegalArgumentException("Invalid product type supplied");
    }

    this.lineId = lineId;
    this.products = products;
    this.efficiency = Math.max(0.0, Math.min(1.0, efficiency));
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
}
