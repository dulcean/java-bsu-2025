package main;

import analyzer.ProdAnalyzer;
import model.line.*;
import model.product.*;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ElectronicsLine elLine1 = new ElectronicsLine("EL-001", 0.95);
        MechanicalLine mechLine1 = new MechanicalLine("MECH-001", 0.88);
        MechanicalLine mechLine2 = new MechanicalLine("MECH-002", 0.92);
        ChemicalLine chemLine1 = new ChemicalLine("CHEM-001", 0.75);
        elLine1.addProduct(new ElectronicProduct("E-CPU-1", "Processor i9", 10));
        elLine1.addProduct(new ElectronicProduct("E-GPU-2", "Video Card RTX", 15));
        mechLine1.addProduct(new MechanicalProduct("M-ENG-1", "V8 Engine", 120));
        mechLine2.addProduct(new MechanicalProduct("M-BR-1", "Brake System", 45));
        mechLine2.addProduct(new MechanicalProduct("M-TR-2", "Transmission", 90));
        chemLine1.addProduct(new ChemicalProduct("C-POL-1", "Polyethylene", 25));
        List<ProductionLine<? extends Product>> allLines = List.of(elLine1, mechLine1, mechLine2, chemLine1);
        ProdAnalyzer analyzer = new ProdAnalyzer(allLines);
        System.out.println("--- Линии с эффективностью > 0.9 ---");
        List<String> highEffLines = analyzer.getHighEfficiencyLines(0.9);
        System.out.println(highEffLines);
        System.out.println("\n--- Количество продуктов по категориям ---");
        Map<String, Long> counts = analyzer.countProductsByCategory();
        System.out.println(counts);
        System.out.println("\n--- Самая загруженная линия ---");
        analyzer.findMostLoadedLine().ifPresent(line ->
                System.out.println("ID: " + line.getLineId() + ", Products: " + line.getProducts().size())
        );
        System.out.println("\n--- Общее время производства всех изделий ---");
        int totalTime = analyzer.calculateTotalProductionTime();
        System.out.println("Total time: " + totalTime + " minutes");
        System.out.println("\n--- Все продукты со всех линий ---");
        List<Product> allProducts = analyzer.getAllProductsFromLines();
        allProducts.forEach(System.out::println);
    }
}