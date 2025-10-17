package bsu.fpmi.nikonchik.javalabs.lab1.products;

public final class MechanicalProduct extends Product {
    private static final String CATEGORY = "Mechanical";

    public MechanicalProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }
}
