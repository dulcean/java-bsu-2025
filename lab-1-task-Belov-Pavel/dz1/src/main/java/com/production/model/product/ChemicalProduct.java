package main.java.com.production.model.product;

public final class ChemicalProduct extends Product {

    public ChemicalProduct(String id, String name, int productionTimeMinutes) {
        super(id, name, productionTimeMinutes);
    }

    @Override
    public String getCategory() {
        return "Chemical";
    }
}