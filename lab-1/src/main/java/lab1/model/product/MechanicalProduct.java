package lab1.model.product;

public final class MechanicalProduct extends Product {
    public MechanicalProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return "Mechanical";
    }
}
