package lab1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProdAnalyzer {
  private final List<ProductionLine<? extends Product>> allLines;

  public ProdAnalyzer(List<ProductionLine<? extends Product>> allLines) {
    this.allLines = Objects.requireNonNull(allLines);
  }

  public List<ProductionLine<? extends Product>> getAllLines() {
    return new ArrayList<>(allLines);
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
        .map(Product::getCategory)
        .collect(Collectors.groupingBy(
            category -> category,
            Collectors.counting()));
  }

  public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
    return allLines.stream()
        .max(Comparator.comparing(line -> line.getProducts().size()));
  }

  public List<Product> getAllProductsFromLines() {
    return allLines.stream()
        .flatMap(line -> line.getProducts().stream())
        .collect(Collectors.toList());
  }

  public double calculateTotalProductionTime() {
    // Idk why task mentions minutes
    return allLines.stream()
        .mapToDouble(
            line -> (line.efficiency * line.getProducts().stream().mapToDouble(x -> x.getProductionTime()).sum()))
        .sum();
  }
}
