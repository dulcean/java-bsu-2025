package com.bsu.production;

import com.bsu.production.analyzer.ProdAnalyzer;
import com.bsu.production.line.ChemicalLine;
import com.bsu.production.line.ElectronicsLine;
import com.bsu.production.line.MechanicalLine;
import com.bsu.production.line.ProductionLine;
import com.bsu.production.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Production Line System Demo ===\n");

        ElectronicsLine electronicsLine1 = new ElectronicsLine("EL-001", 0.85);
        ElectronicsLine electronicsLine2 = new ElectronicsLine("EL-002", 0.92);
        MechanicalLine mechanicalLine = new MechanicalLine("ML-001", 0.78);
        ChemicalLine chemicalLine = new ChemicalLine("CL-001", 0.65);

        ElectronicProduct laptop = new ElectronicProduct("E001", "Laptop", 120);
        ElectronicProduct smartphone = new ElectronicProduct("E002", "Smartphone", 90);
        ElectronicProduct tablet = new ElectronicProduct("E003", "Tablet", 100);
        
        MechanicalProduct engine = new MechanicalProduct("M001", "Engine", 200);
        MechanicalProduct gear = new MechanicalProduct("M002", "Gear", 45);
        
        ChemicalProduct paint = new ChemicalProduct("C001", "Industrial Paint", 60);
        ChemicalProduct solvent = new ChemicalProduct("C002", "Solvent", 30);

        System.out.println("Adding products to production lines...");
        electronicsLine1.addProduct(laptop);
        electronicsLine1.addProduct(smartphone);
        
        electronicsLine2.addProduct(tablet);
        
        mechanicalLine.addProduct(engine);
        mechanicalLine.addProduct(gear);
        
        chemicalLine.addProduct(paint);
        chemicalLine.addProduct(solvent);

        System.out.println("Products added successfully\n");

        System.out.println("Testing validation...");
        try {
            electronicsLine1.validate(engine);
            System.out.println("Validation failed - should have thrown exception");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation working: " + e.getMessage());
        }
        System.out.println();

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(electronicsLine1);
        analyzer.addLine(electronicsLine2);
        analyzer.addLine(mechanicalLine);
        analyzer.addLine(chemicalLine);

        System.out.println("=== Stream API Operations ===\n");

        System.out.println("1. Lines with efficiency > 0.80:");
        List<String> highEfficiencyLines = analyzer.getHighEfficiencyLines(0.80);
        for (String id : highEfficiencyLines) {
            System.out.println("   - " + id);
        }
        System.out.println();

        System.out.println("2. Products by category:");
        Map<String, Long> categoryCount = analyzer.countProductsByCategory();
        for (Map.Entry<String, Long> entry : categoryCount.entrySet()) {
            System.out.println("   - " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        System.out.println("3. Most loaded production line:");
        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        if (mostLoaded.isPresent()) {
            ProductionLine<? extends Product> line = mostLoaded.get();
            System.out.println("   - " + line.getLineId() + " with " + line.getProducts().size() + " products");
        }
        System.out.println();

        System.out.println("4. All products from all lines:");
        List<Product> allProducts = analyzer.getAllProductsFromLines();
        for (Product product : allProducts) {
            System.out.println("   - " + product.getName() + " (" + product.getCategory() + ")");
        }
        System.out.println();

        System.out.println("5. Total production time:");
        double totalTime = analyzer.calculateTotalProductionTime();
        System.out.println("   - " + totalTime + " minutes");
        System.out.println();

        System.out.println("=== System Statistics ===\n");
        System.out.println(analyzer.getStatisticsSummary());
    }
}
