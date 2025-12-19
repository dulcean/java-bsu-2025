package products;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record ProdAnalyzer(List<ProductionLine<? extends Product>> lines) {

    public List<String> getHighEfficiencyLines(double limit) {
        return lines.stream()
                .filter(l -> l.getEfficiency() > limit)
                .map(ProductionLine::getLineCode)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countProductsByCategory() {
        return lines.stream()
                .flatMap(l -> l.getItems().stream())
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return lines.stream()
                .max(Comparator.comparingInt(l -> l.getItems().size()));
    }

    public List<Product> getAllProductsFromLines() {
        return lines.stream()
                .flatMap(l -> l.getItems().stream())
                .collect(Collectors.toList());
    }

    public int calculateTotalProductionTime() {
        return lines.stream()
                .flatMap(l -> l.getItems().stream())
                .mapToInt(Product::getProductionMinutes)
                .sum();
    }
}