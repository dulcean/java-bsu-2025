package lab1.model.product;

public final class ChemicalProduct extends Product {
    public ChemicalProduct(String id, String name, int productionTime) {
        super(id, name, productionTime);
    }

    @Override
    public String getCategory() {
        return "Chemical";
    }
}
