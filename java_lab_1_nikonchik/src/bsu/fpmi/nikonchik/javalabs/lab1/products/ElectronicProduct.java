package bsu.fpmi.nikonchik.javalabs.lab1.products;

public final class ElectronicProduct extends Product {
    private static final String CATEGORY = "Electronics";

    public ElectronicProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }
}