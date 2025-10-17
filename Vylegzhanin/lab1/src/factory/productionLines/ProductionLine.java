package factory.productionLines;

import factory.FactoryOperationResult;
import factory.products.ChemicalProduct;
import factory.products.ElectronicProduct;
import factory.products.MechanicalProduct;
import factory.products.Product;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String id;
    double efficiency;
    private List<T> products;

    public ProductionLine(String id, double efficiency) {
        this.id = id;
        this.efficiency = efficiency;
        this.products = new ArrayList<T>();
    }

    public String getId() {
        return id;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public List<T> getProducts() {
        return products;
    }

    boolean canProduce(Product product) {
        return false;
    }


    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public void addProduct(Product product) {
        if (!canProduce(product)) {
            throw new IllegalArgumentException();
        }
        products.add((T)product);
    }

    public void removeProduct(int product) {
        products.remove(product);
    }

    public FactoryOperationResult addProduct(String id, String name, int time, String type) {
        if (products.stream().map(Product::getId).anyMatch(id::contentEquals)) {
            return FactoryOperationResult.ID_FAILURE;
        }

        try {
            switch (type.toLowerCase()) {
                case "chemical":
                case "c": {
                    addProduct(new ChemicalProduct(id, name, time));
                    break;
                }
                case "electronic":
                case "e": {
                    addProduct(new ElectronicProduct(id, name, time));
                    break;
                }
                case "mechanic":
                case "m": {
                    addProduct(new MechanicalProduct(id, name, time));
                    break;
                }
                default: {
                    return FactoryOperationResult.TYPE_FAILURE;
                }
            }
        } catch (IllegalArgumentException e) {
            return FactoryOperationResult.TYPE_FAILURE;
        }

        return FactoryOperationResult.SUCCESS;
    }

    public abstract String getCategory();
}
