package com.bsu.plant.planttest;

import com.bsu.plant.analyzer.ProdAnalyzer;
import com.bsu.plant.product.*;
import com.bsu.plant.productlines.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("===================================== PRODUCTLINES TESTS ================================\n");
        
        try {
            testBasicFunctionality();
            testValidationAndErrors();
            testEdgeCases();
            testPerformanceAndCalculations();
            
        } catch (Exception e) {
            System.err.println("Executing tests error: " + e.getMessage());
        }
    }
    
    private static void testBasicFunctionality() {
        System.out.println("===================================== TEST 1: BASICS ====================================");
        
        var analyzer = createTestAnalyzer();
        
        System.out.println("\nLines with efficiency > 0.85: " + 
                          analyzer.getHighEfficiencyLines(0.85));
        System.out.println("Lines with efficiency > 0.95: " + 
                          analyzer.getHighEfficiencyLines(0.95));
        
        System.out.println("\nCount of products by categories: " + 
                          analyzer.countProductsByCategory());
        
        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        mostLoaded.ifPresent(line -> 
            System.out.println("\nThe most overloaded line: " + line.getLineId() + 
                             " (products: " + line.getProducts().size() + ")")
        );
        
        List<Product> allProducts = analyzer.getAllProductsFromLines();
        System.out.println("\nAll the products (" + allProducts.size() + " things):");
        allProducts.forEach(product -> 
            System.out.println("   - " + product.getName() + " (" + product.getCategory() + ")")
        );
        
        System.out.println("\nGeneral time of production: " + 
                          analyzer.calculateTotalProductionTime() + " minutes");
        
        System.out.println("\nAll the production lines:");
        analyzer.getAllLines().forEach(line -> 
            System.out.println("   - " + line.getLineId() + 
                             " (eff.: " + String.format("%.2f", line.getEfficiency()) + 
                             ", products: " + line.getProducts().size() + ")")
        );
    }
    
    private static void testValidationAndErrors() {
        System.out.println("\n\n=============================== TEST 2: VALIDATIONS =================================");
        
        try {
            var mechLine = new MechanicalLine("MECH-TEST", 0.8);
            mechLine.addProduct(null);
            System.err.println("ERROR: Null product is added");
        } catch (IllegalArgumentException e) {
            System.out.println("OK of not adding null product: " + e.getMessage());
        }
        
        try {
            new ElectronicsLine("EL-INVALID", 1.5); // Effectivity > 1.0
            System.err.println("ERROR of invalid eff");
        } catch (IllegalArgumentException e) {
            System.out.println("OK of wrong effectivety : " + e.getMessage());
        }
        
        try {
            new ElectronicsLine("", 0.8);
            System.err.println("ERROR of wrong ID");
        } catch (IllegalArgumentException e) {
            System.out.println("OK of checking ID: " + e.getMessage());
        }
    }
    
    private static void testEdgeCases() {
        System.out.println("\n\n=============================== TEST 3: BOUND CASES =================================");
        
        try {
            var emptyAnalyzer = new ProdAnalyzer(Arrays.asList());
            System.out.println("Empty analyzer is build");
            System.out.println("   - High efficiency lines: " + emptyAnalyzer.getHighEfficiencyLines(0.5));
            System.out.println("   - Products by categories: " + emptyAnalyzer.countProductsByCategory());
            System.out.println("   - The most loaded line: " + emptyAnalyzer.findMostLoadedLine());
            System.out.println("   - The general time: " + emptyAnalyzer.calculateTotalProductionTime());
        } catch (Exception e) {
            System.err.println("ERROR of empty analyzer: " + e.getMessage());
        }
        
        var singleLine = new ElectronicsLine("EL-SINGLE", 0.99);
        singleLine.addProduct(new ElectronicProduct("E999", "Single Product", 50));
        var singleAnalyzer = new ProdAnalyzer(Arrays.asList(singleLine));
        
        System.out.println("\nOne line analyzer:");
        System.out.println("   - High efficiency lines: " + singleAnalyzer.getHighEfficiencyLines(0.5));
        System.out.println("   - The most loaded line: " + 
                          singleAnalyzer.findMostLoadedLine().get().getLineId());
        
        var lowEffLine = new ElectronicsLine("EL-LOW", 0.0);
        var highEffLine = new ElectronicsLine("EL-HIGH", 1.0);
        System.out.println("\n3.3 Bound cases of efficiency:");
        System.out.println("   - 0% efficiency line: " + lowEffLine.getEfficiency());
        System.out.println("   - 100% efficiency line: " + highEffLine.getEfficiency());
    }
    
    private static void testPerformanceAndCalculations() {
        System.out.println("\n\n======================= TEST 4: EFFICIENCY AND CALCULATIONS =========================");
        
        var analyzer = createLargeTestAnalyzer(); //это мега загруженный анализатор
        
        System.out.println("Big data stores statistics:");
        System.out.println("   - All the functions: " + analyzer.getAllLines().size());
        System.out.println("   - All the products: " + analyzer.getAllProductsFromLines().size());
        System.out.println("   - General time of production: " + analyzer.calculateTotalProductionTime() + " minutes");
        
        var categoryStats = analyzer.countProductsByCategory();
        System.out.println("\nStatistics of categories:");
        categoryStats.forEach((category, count) -> 
            System.out.println("   - " + category + ": " + count + " products")
        );
        
        System.out.println("\nLines efficiency analisis:");
        for (double threshold = 0.7; threshold <= 1.0; threshold += 0.1) {
            var efficientLines = analyzer.getHighEfficiencyLines(threshold);
            System.out.println("   - Lines with effectivity > " + 
                             String.format("%.1f", threshold) + ": " + efficientLines.size());
        }
        
        var testLine = new ElectronicsLine("EL-TEST-CANPRODUCE", 0.9);
        var electronicProduct = new ElectronicProduct("E-TEST", "Test Device", 100);
        var mechanicalProduct = new MechanicalProduct("M-TEST", "Test Part", 150);
        
        System.out.println("\ncanProduce method test:");
        System.out.println("   - Electronic line + Electronic product: " + 
                          testLine.canProduce(electronicProduct));
        System.out.println("   - Electronic line + Mechanical product: " + 
                          testLine.canProduce(mechanicalProduct));
    }
    
    private static ProdAnalyzer createTestAnalyzer() {
        var smartphone = new ElectronicProduct("E001", "Smartphone", 120);
        var laptop = new ElectronicProduct("E002", "Laptop", 180);
        var tablet = new ElectronicProduct("E003", "Tablet", 90);
        var smartwatch = new ElectronicProduct("E004", "Smartwatch", 60);
        
        var engine = new MechanicalProduct("M001", "Car Engine", 300);
        var gear = new MechanicalProduct("M002", "Transmission Gear", 45);
        var piston = new MechanicalProduct("M003", "Piston", 30);
        
        var fertilizer = new ChemicalProduct("C001", "Fertilizer", 90);
        var plastic = new ChemicalProduct("C002", "Plastic Polymer", 60);
        var solvent = new ChemicalProduct("C003", "Industrial Solvent", 75);
        
        var highEffElectronics = new ElectronicsLine("EL-HIGH", 0.96);
        var mediumEffElectronics = new ElectronicsLine("EL-MED", 0.82);
        var highEffMechanical = new MechanicalLine("MECH-HIGH", 0.94);
        var lowEffMechanical = new MechanicalLine("MECH-LOW", 0.78);
        var chemicalLine = new ChemicalLine("CHEM-01", 0.89);
        
        highEffElectronics.addProduct(smartphone);
        highEffElectronics.addProduct(laptop);
        mediumEffElectronics.addProduct(tablet);
        mediumEffElectronics.addProduct(smartwatch);
        
        highEffMechanical.addProduct(engine);
        highEffMechanical.addProduct(gear);
        lowEffMechanical.addProduct(piston);
        
        chemicalLine.addProduct(fertilizer);
        chemicalLine.addProduct(plastic);
        chemicalLine.addProduct(solvent);
        
        return new ProdAnalyzer(Arrays.asList(
            highEffElectronics, mediumEffElectronics, 
            highEffMechanical, lowEffMechanical, chemicalLine
        ));
    }
    
    private static ProdAnalyzer createLargeTestAnalyzer() {
        var lines = new java.util.ArrayList<ProductionLine<? extends Product>>();
        
        for (int i = 1; i <= 3; i++) {
            var elLine = new ElectronicsLine("EL-BATCH-" + i, 0.8 + (i * 0.05));
            for (int j = 1; j <= 5; j++) {
                elLine.addProduct(new ElectronicProduct("E" + i + "-" + j, 
                    "Device " + i + "-" + j, 100 + (i * j * 10)));
            }
            lines.add(elLine);
        }
        
        for (int i = 1; i <= 2; i++) {
            var mechLine = new MechanicalLine("MECH-BATCH-" + i, 0.75 + (i * 0.08));
            for (int j = 1; j <= 4; j++) {
                mechLine.addProduct(new MechanicalProduct("M" + i + "-" + j,
                    "Part " + i + "-" + j, 50 + (i * j * 15)));
            }
            lines.add(mechLine);
        }
        
        for (int i = 1; i <= 2; i++) {
            var chemLine = new ChemicalLine("CHEM-BATCH-" + i, 0.85 + (i * 0.06));
            for (int j = 1; j <= 3; j++) {
                chemLine.addProduct(new ChemicalProduct("C" + i + "-" + j,
                    "Compound " + i + "-" + j, 80 + (i * j * 12)));
            }
            lines.add(chemLine);
        }
        
        return new ProdAnalyzer(lines);
    }
}