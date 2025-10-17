package factory;

import factory.productionLines.ElectronicsLine;
import factory.productionLines.ProductionLine;
import factory.products.Product;

import java.util.*;

public class ProdAnalyzer {
    private List<ProductionLine<? extends Product>> lines;

    public ProdAnalyzer() {
        lines = new ArrayList<>();
    }

    public int CalculateTotalProductionTime() {
        return lines.stream().flatMap(line -> line.getProducts().stream()).map(Product::getProductionTime).reduce(0 , Integer::sum);
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return lines;
    }

    public void insertLine(ProductionLine<? extends Product> line) {
        lines.add(line);
    }

    public void removeLine (ProductionLine<? extends Product> line) {
        lines.remove(line);
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return lines.stream().filter(line->line.getEfficiency() > threshold).map(ProductionLine::getId).toList();
    }

    public Map<String, Long> countProductsByCategory () {
        Map<String, Long> map = new HashMap<>();
        lines.stream().flatMap(line->line.getProducts().stream()).map(Product::getCategory).forEach(category -> map.put(category, map.getOrDefault(category, 0L) + 1));
        return map;
    }

    public List<? extends Product> getAllProductsFromLines() {
        return lines.stream().flatMap(line->line.getProducts().stream()).toList();
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return lines.stream().max(Comparator.comparingInt(line -> line.getProducts().size()));
    }
}
