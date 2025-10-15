package by.bsu.factory.service;

import by.bsu.factory.product.Product;
import by.bsu.factory.line.ProductionLine;

import java.util.*;

public final class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> allLines;

    public ProdAnalyzer(List<ProductionLine<? extends Product>> allLines) {
        if (allLines == null) throw new IllegalArgumentException("allLines");
        this.allLines = List.copyOf(allLines);
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return allLines;
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        List<String> result = new ArrayList<>();
        for (ProductionLine<? extends Product> line : allLines) {
            if (line.getEfficiency() > threshold) {
                result.add(line.getLineId());
            }
        }
        return result;
    }

    public Map<String, Long> countProductsByCategory() {
        Map<String, Long> result = new HashMap<>();
        for (ProductionLine<? extends Product> line : allLines) {
            for (Product product : line.getProducts()) {
                String category = product.getCategory();
                long count = result.getOrDefault(category, 0L);
                result.put(category, count + 1);
            }
        }
        return result;
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        if (allLines.isEmpty()) return Optional.empty();
        ProductionLine<? extends Product> maxLine = allLines.get(0);
        for (ProductionLine<? extends Product> line : allLines) {
            if (line.getProducts().size() > maxLine.getProducts().size()) {
                maxLine = line;
            }
        }
        return Optional.of(maxLine);
    }

    public List<Product> getAllProductsFromLines() {
        List<Product> result = new ArrayList<>();
        for (ProductionLine<? extends Product> line : allLines) {
            for (Product product : line.getProducts()) {
                result.add(product);
            }
        }
        return result;
    }

    public double calculateTotalProductionTime() {
        double total = 0.0;
        for (ProductionLine<? extends Product> line : allLines) {
            for (Product product : line.getProducts()) {
                total += product.getProductionTime();
            }
        }
        return total;
    }
}
