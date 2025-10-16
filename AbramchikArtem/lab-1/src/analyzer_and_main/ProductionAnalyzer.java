package analyzer_and_main;

import models.product.Product;
import models.product.ElectronicProduct;
import models.product.MechanicalProduct;
import models.product.ChemicalProduct;
import models.productionline.ProductionLine;
import models.productionline.ElectronicsLine;
import models.productionline.MechanicalLine;
import models.productionline.ChemicalLine;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductionAnalyzer {
    private List<ProductionLine<? extends Product>> allLines;

    public ProductionAnalyzer(List<ProductionLine<? extends Product>> allLines) {
        this.allLines = allLines;
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return allLines;
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return allLines.stream()
                .filter(line -> line.getEfficiency() > threshold)
                .map(ProductionLine::getLineId)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countProductsByCategory() {
        return allLines.stream()
                .flatMap(line -> line.getProducts().stream()) // Разворачиваем все продукты со всех линий
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

    public static void main(String[] args) {
        Product laptop = new ElectronicProduct("E001", "Laptop", 120);
        Product smartphone = new ElectronicProduct("E002", "Smartphone", 90);
        Product gears = new MechanicalProduct("M001", "Gears", 60);
        Product engine = new MechanicalProduct("M002", "Engine", 240);
        Product acid = new ChemicalProduct("C001", "Sulfuric Acid", 30);
        Product polymer = new ChemicalProduct("C002", "Polymer", 180);
        Product tablet = new ElectronicProduct("E003", "Tablet", 75);

        ProductionLine<ElectronicProduct> electronicsLine1 = new ElectronicsLine("EL-01", 0.85);
        ProductionLine<ElectronicProduct> electronicsLine2 = new ElectronicsLine("EL-02", 0.92);
        ProductionLine<MechanicalProduct> mechanicalLine1 = new MechanicalLine("ML-01", 0.78);
        ProductionLine<ChemicalProduct> chemicalLine1 = new ChemicalLine("CL-01", 0.95);

        electronicsLine1.addProduct((ElectronicProduct) laptop);
        electronicsLine1.addProduct((ElectronicProduct) smartphone);
        electronicsLine1.addProduct((ElectronicProduct) tablet);

        electronicsLine2.addProduct((ElectronicProduct) smartphone);
        electronicsLine2.addProduct((ElectronicProduct) laptop);

        mechanicalLine1.addProduct((MechanicalProduct) gears);
        mechanicalLine1.addProduct((MechanicalProduct) engine);

        chemicalLine1.addProduct((ChemicalProduct) acid);
        chemicalLine1.addProduct((ChemicalProduct) polymer);
        chemicalLine1.addProduct((ChemicalProduct) acid);


        List<ProductionLine<? extends Product>> lines = List.of(electronicsLine1, electronicsLine2, mechanicalLine1, chemicalLine1);
        ProductionAnalyzer analyzer = new ProductionAnalyzer(lines);

        System.out.println("--- Все линии ---");
        analyzer.getAllLines().forEach(System.out::println);
        System.out.println();

        System.out.println("--- Линии с эффективностью выше 0.9 ---");
        analyzer.getHighEfficiencyLines(0.9).forEach(System.out::println);
        System.out.println();

        System.out.println("--- Количество продуктов по категориям ---");
        analyzer.countProductsByCategory().forEach((category, count) ->
                System.out.println("Категория: " + category + ", Количество: " + count));
        System.out.println();

        System.out.println("--- Самая загруженная линия ---");
        analyzer.findMostLoadedLine().ifPresentOrElse(
                line -> System.out.println("Линия с наибольшим количеством изделий: " + line.getLineId() + " (изделий: " + line.getProducts().size() + ")"),
                () -> System.out.println("Линии не найдены."));
        System.out.println();

        System.out.println("--- Все продукты со всех линий ---");
        analyzer.getAllProductsFromLines().forEach(System.out::println);
    }
}