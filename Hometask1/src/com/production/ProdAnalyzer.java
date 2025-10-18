package com.production;

import com.production.lines.ProductionLine;
import com.production.products.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProdAnalyzer {

    private final List<ProductionLine<? extends Product>> allLines;

    public ProdAnalyzer(List<ProductionLine<? extends Product>> allLines) {
        this.allLines = new ArrayList<>(allLines);
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
        return getAllProductsFromLines().stream()
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
        return (List<Product>) allLines.stream()
                .flatMap(line -> line.getProducts().stream())
                .collect(Collectors.toList());
    }

    public double calculateTotalProductionTime() {
        return getAllProductsFromLines().stream()
                .mapToInt(Product::productionTime)
                .sum();
    }
}
