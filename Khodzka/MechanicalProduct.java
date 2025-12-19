package products;

public class MechanicalProduct extends Product {
    public MechanicalProduct(String id, String title, int productionMinutes) {
        super(id, title, productionMinutes);
    }

    @Override
    public String getCategory() {
        return "Mechanical";
    }
}