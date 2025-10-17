package Krohau.service;

import Krohau.model.Product;
import Krohau.model.ProductionLine;

import java.util.*;
import java.util.stream.Collectors;

public record ProdAnalyzer(List<ProductionLine<? extends Product>> allLines) {

    public ProdAnalyzer {
        allLines = Collections.unmodifiableList(allLines != null ? new ArrayList<>(allLines) : new ArrayList<>());
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        if (threshold < 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("Порог эффективности должен быть от 0.0 до 1.0.");
        }
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

    public int calculateTotalProductionTime() {
        return allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .mapToInt(Product::getProductionTime)
                .sum();
    }
}