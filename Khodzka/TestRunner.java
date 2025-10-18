package products;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestRunner {
public static void main(String[] args) {
    var analyzer = createSampleAnalyzer();
    testCanProduce();
    testEfficiencyFilter(analyzer);
    testCountProductsByCategory(analyzer);
    testFindMostLoadedLine(analyzer);
    testAllProductsFromLines(analyzer);
    testTotalProductionTime(analyzer);
}

private static ProdAnalyzer createSampleAnalyzer() {
    var p1 = new ElectronicProduct("E01", "Phone", 100);
    var p2 = new ElectronicProduct("E02", "Laptop", 200);
    var p3 = new MechanicalProduct("M01", "Gearbox", 300);
    var p4 = new ChemicalProduct("C01", "Paint", 80);
    var electronics = new ElectronicLine("LINE-E", 0.95);
    var mechanical = new MechanicalLine("LINE-M", 0.85);
    var chemical = new ChemicalLine("LINE-C", 0.90);
    electronics.addProduct(p1);
    electronics.addProduct(p2);
    mechanical.addProduct(p3);
    chemical.addProduct(p4);
    return new ProdAnalyzer(List.of(electronics, mechanical, chemical));
}

private static void testCanProduce() {
    var electronics = new ElectronicLine("EL-1", 0.9);
    var mechanical = new MechanicalLine("MECH-1", 0.8);
    var laptop = new ElectronicProduct("E100", "Laptop", 150);
    var engine = new MechanicalProduct("M100", "Engine", 250);
    System.out.println("Electronics line can produce Laptop? " + electronics.canProduce(laptop));
    System.out.println("Electronics line can produce Engine? " + electronics.canProduce(engine));
    System.out.println("Mechanical line can produce Engine? " + mechanical.canProduce(engine));
}

private static void testEfficiencyFilter(ProdAnalyzer analyzer) {
    var result = analyzer.getHighEfficiencyLines(0.9);
    System.out.println("Expected lines with efficiency > 0.9: " + result);
}

private static void testCountProductsByCategory(ProdAnalyzer analyzer) {
    var counts = analyzer.countProductsByCategory();
    System.out.println("Product count by category: " + counts);
}

private static void testFindMostLoadedLine(ProdAnalyzer analyzer) {
    Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
    mostLoaded.ifPresent(line -> System.out.println("Most loaded line: " + line.getLineId()));
}

private static void testAllProductsFromLines(ProdAnalyzer analyzer) {
    var allProducts = analyzer.getAllProductsFromLines();
    System.out.println("All products collected: " + allProducts);
}

private static void testTotalProductionTime(ProdAnalyzer analyzer) {
    double totalTime = analyzer.calculateTotalProductionTime();
    System.out.println("Total production time (min): " + totalTime);
}
}
