package com.bsu.production.analyzer;

import com.bsu.production.line.ProductionLine;
import com.bsu.production.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> allLines = new ArrayList<>();

    public void addLine(ProductionLine<? extends Product> line) {
        allLines.add(Objects.requireNonNull(line));
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
}
