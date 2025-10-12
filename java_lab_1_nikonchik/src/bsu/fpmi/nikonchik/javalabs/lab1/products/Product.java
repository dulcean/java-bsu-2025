package bsu.fpmi.nikonchik.javalabs.lab1.products;

public abstract class Product {
    protected Product(String id, String name, int productionTime) {
        this.info = new ProductInfo(id, name, productionTime);
    }

    public abstract String getCategory();

    public String getId() {
        return info.id();
    }
    public String getName() {
        return info.name();
    }
    public int getProductionTime() {
        return info.productionTime(); //seconds
    }

    private record ProductInfo(String id, String name, int productionTime) {}
    //
    private ProductInfo info;
}
