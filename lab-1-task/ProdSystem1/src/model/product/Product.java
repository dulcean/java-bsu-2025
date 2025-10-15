package model.product;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    public Product(String id, String name, int productionTime) {
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
    }

    public abstract String getCategory();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + getCategory() + '\'' +
                '}';
    }
}