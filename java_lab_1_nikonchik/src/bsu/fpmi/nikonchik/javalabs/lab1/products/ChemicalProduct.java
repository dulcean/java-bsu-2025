package bsu.fpmi.nikonchik.javalabs.lab1.products;

public final class ChemicalProduct extends Product {
    private static final String CATEGORY = "Chemical";

    public ChemicalProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }
}
