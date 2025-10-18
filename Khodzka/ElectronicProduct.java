package products;

public class ElectronicProduct extends Product {
    public ElectronicProduct(String id, String title, int productionMinutes) {
        super(id, title, productionMinutes);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}