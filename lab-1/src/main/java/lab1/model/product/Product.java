package lab1.model.product;

/**
 * Абстрактный класс, для продуктов. Record не получится использовать.
 */
public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    protected Product(String id, String name, int productionTime) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is blank");
        }
        if (productionTime <= 0) {
            throw new IllegalArgumentException("productionTime must be > 0");
        }
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public abstract String getCategory();

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", productionTime=" + productionTime +
                ", category=" + getCategory() +
                '}';
    }
}
