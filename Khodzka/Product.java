package products;

public abstract class Product {
    private final String id;
    private final String title;
    private final int productionMinutes;

    public Product(String id, String title, int productionMinutes) {
        this.id = id;
        this.title = title;
        this.productionMinutes = productionMinutes;
    }

    public abstract String getCategory();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getProductionMinutes() { return productionMinutes; }

    @Override
    public String toString() {
        return title + "(" + id + ", " + productionMinutes + "min)";
    }
}