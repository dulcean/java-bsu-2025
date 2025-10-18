package main.java. com.production.model.product;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTimeMinutes;

    public Product(String id, String name, int productionTimeMinutes) {
        this.id = id;
        this.name = name;
        this.productionTimeMinutes = productionTimeMinutes;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getProductionTimeMinutes() { return productionTimeMinutes; }

    public abstract String getCategory();
}