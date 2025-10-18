package products;

public class ChemicalProduct extends Product {
    public ChemicalProduct(String id, String title, int productionMinutes) {
        super(id, title, productionMinutes);
    }

    @Override
    public String getCategory() {
        return "Chemical";
    }
}