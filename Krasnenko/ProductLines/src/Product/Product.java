package Product;


public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime; // minutes

    protected Product(String id, String name, int productionTime) {
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getProductionTime() { return productionTime; }

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', time=%d}",
                getCategory(), id, name, productionTime);
    }
}
