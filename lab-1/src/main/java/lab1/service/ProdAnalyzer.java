package lab1.service;

import lab1.model.line.ProductionLine;
import lab1.model.product.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> lines;

    public ProdAnalyzer(List<ProductionLine<? extends Product>> lines) {
        this.lines = new ArrayList<>(Objects.requireNonNull(lines));
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return lines.stream()
                .filter(l -> l.getEfficiency() > threshold)
                .map(ProductionLine::getLineId)
                .toList();
    }

    public Map<String, Long> countProductsByCategory() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()
                ));
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return lines.stream()
                .max(Comparator.comparingInt(l -> l.getProducts().size()));
    }

    public List<Product> getAllProductsFromLines() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream().map(p -> (Product) p))
                .toList();
    }

    public int calculateTotalProductionTime() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream())
                .mapToInt(Product::getProductionTime)
                .sum();
    }
}
