package com.bsu.production.analyzer;

import com.bsu.production.line.ProductionLine;
import com.bsu.production.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> allLines;

    public ProdAnalyzer() {
        this.allLines = new ArrayList<>();
    }

    public void addLine(ProductionLine<? extends Product> line) {
        if (line == null) {
            throw new IllegalArgumentException("Production line cannot be null");
        }
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
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()
                ));
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
                .mapToInt(Product::getProductionTime)
                .sum();
    }

    public String getStatisticsSummary() {
        long totalProducts = getAllProductsFromLines().size();
        double avgEfficiency = allLines.stream()
                .mapToDouble(ProductionLine::getEfficiency)
                .average()
                .orElse(0.0);
        
        return String.format("""
                             Production Statistics:
                             - Total Lines: %d
                             - Total Products: %d
                             - Average Efficiency: %.2f
                             - Total Production Time: %.0f minutes""",
                allLines.size(), totalProducts, avgEfficiency, calculateTotalProductionTime()
        );
    }
}
