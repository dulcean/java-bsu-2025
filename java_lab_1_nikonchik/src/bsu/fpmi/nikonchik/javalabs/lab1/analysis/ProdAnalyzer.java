package bsu.fpmi.nikonchik.javalabs.lab1.analysis;

import bsu.fpmi.nikonchik.javalabs.lab1.products.Product;
import bsu.fpmi.nikonchik.javalabs.lab1.productionlines.ProductionLine;

import java.util.*;
import java.util.stream.*;


public class ProdAnalyzer {
    public ProdAnalyzer(List<ProductionLine<? extends Product>> lines) {
        this.allLines = new ArrayList<>(lines);
    }
    public ProdAnalyzer(ProductionLine<? extends Product> ...lines) {
        if (lines == null) {
            this.allLines = new ArrayList<>();
        } else {
            this.allLines = Arrays.stream(lines)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
    public ProdAnalyzer() {
        this.allLines = new ArrayList<>();
    }

    public void addLines(ProductionLine<? extends Product> ...lines) {
        this.allLines.addAll(Arrays.stream(lines)
                .filter(Objects::nonNull)
                .toList());
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return Collections.unmodifiableList(this.allLines);
    }

    public Map<String, Long> countProductsByCategory() {
        Stream<? extends Product> allProductsStream = this.allLines.stream()
                .flatMap(productionLine -> productionLine.getAllProducts().stream());

        Map<String, Long> resMap = allProductsStream.collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.counting()
        ));

        return resMap;
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        List<String> highEfficiencyLines = this.allLines.stream()
                .filter(line -> line.getEfficiency() > threshold)
                .map(line -> line.getLineId())
                .collect(Collectors.toUnmodifiableList());

        return highEfficiencyLines;
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        if (this.allLines.isEmpty()) {
            return Optional.empty();
        } else {
            return this.allLines.stream()
                    .max(Comparator.comparingInt(ProductionLine::countProducts));
        }
    }

    public List<? extends Product> getAllProductsFromLines() {
        List<? extends Product> allProducts = this.allLines.stream()
                .flatMap(productionLine -> productionLine.getAllProducts().stream())
                .collect(Collectors.toList());
        return allProducts;
    }

    public double calculateTotalProductionTimeInMinutes() {
        final Integer secondsInMinute = 60;
        List<? extends Product> allProducts = getAllProductsFromLines();
        int totalTimeInSeconds = allProducts.stream()
                .collect(Collectors.summingInt(Product::getProductionTime));
        return ((double) totalTimeInSeconds) / secondsInMinute;
    }

    private List<ProductionLine<? extends Product>> allLines;
}
