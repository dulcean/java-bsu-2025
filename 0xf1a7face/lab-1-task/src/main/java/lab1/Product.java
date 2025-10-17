package lab1;

public abstract class Product {
    protected String id;
    protected String name;
    protected int productionTime;

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

    public abstract String getCategory();
}
