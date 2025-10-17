package by.bsu.factory.product;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    protected Product(String id, String name, int productionTime) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (productionTime < 0) throw new IllegalArgumentException("productionTime");
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
}
