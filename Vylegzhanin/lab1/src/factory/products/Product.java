package factory.products;

public abstract class Product {
    private String id;
    private String name;
    private int productionTime;
    public abstract String getCategory ();

    public Product(String id, String name, int productionTime) {
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
}