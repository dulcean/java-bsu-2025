package org.example;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    protected final List<T> products = new ArrayList<>();
    private double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        if (lineId == null || lineId.isBlank()) throw new IllegalArgumentException("lineId required");
        setEfficiency(efficiency);
        this.lineId = lineId;
    }

    public String getLineId() { return lineId; }
    public List<T> getProducts() { return Collections.unmodifiableList(products); }
    public double getEfficiency() { return efficiency; }

    public void setEfficiency(double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0)
            throw new IllegalArgumentException("efficiency must be in [0.0, 1.0]");
        this.efficiency = efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        Objects.requireNonNull(product);
        if (!canProduce(product)) {
            throw new IllegalArgumentException("Product " + product + " is not compatible with line " + lineId);
        }
        products.add(product);
    }

    public boolean removeProduct(T product) {
        return products.remove(product);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[lineId=" + lineId +
                ", eff=" + efficiency + ", products=" + products.size() + "]";
    }
}

class ElectronicsLine extends ProductionLine<ElectronicProduct> {
    public ElectronicsLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ElectronicProduct;
    }
}

class MechanicalLine extends ProductionLine<MechanicalProduct> {
    public MechanicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof MechanicalProduct;
    }
}

class ChemicalLine extends ProductionLine<ChemicalProduct> {
    public ChemicalLine(String lineId, double efficiency) {
        super(lineId, efficiency);
    }

    @Override
    public boolean canProduce(Product product) {
        return product instanceof ChemicalProduct;
    }
}

class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> allLines = new ArrayList<>();

    public ProdAnalyzer() {}

    public void registerLine(ProductionLine<? extends Product> line) {
        Objects.requireNonNull(line);
        allLines.add(line);
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return Collections.unmodifiableList(allLines);
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return allLines.stream()
                .filter(line -> line.getEfficiency() > threshold)
                .map(ProductionLine::getLineId)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countProductsByCategory() {
        return allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return allLines.stream()
                .max(Comparator.comparingInt(line -> line.getProducts().size()));
    }

    public List<Product> getAllProductsFromLines() {
        return allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .collect(Collectors.toList());
    }

    public double calculateTotalProductionTime() {
        return allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .mapToDouble(Product::getProductionTimeMinutes)
                .sum();
    }
}
