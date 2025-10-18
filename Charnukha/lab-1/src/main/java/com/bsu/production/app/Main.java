package com.bsu.production.app;

import com.bsu.production.analyzer.ProdAnalyzer;
import com.bsu.production.line.*;
import com.bsu.production.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        ElectronicsLine eLine = new ElectronicsLine("E-01", 0.95);
        MechanicalLine mLine = new MechanicalLine("M-01", 0.7);
        ChemicalLine cLine = new ChemicalLine("C-01", 0.6);

        ElectronicProduct e1 = new ElectronicProduct("E100", "PhoneBoard", 30);
        ElectronicProduct e2 = new ElectronicProduct("E101", "CameraModule", 20);

        MechanicalProduct m1 = new MechanicalProduct("M200", "Gear", 15);
        MechanicalProduct m2 = new MechanicalProduct("M201", "Shaft", 10);
        MechanicalProduct m3 = new MechanicalProduct("M202", "Bracket", 8);

        ChemicalProduct c1 = new ChemicalProduct("C300", "Coating", 120);

        addIfCan(eLine, e1);
        addIfCan(eLine, e2);

        addIfCan(mLine, m1);
        addIfCan(mLine, m2);
        addIfCan(mLine, m3);

        addIfCan(cLine, c1);

        MechanicalProduct wrong = new MechanicalProduct("M999", "WrongForElectronics", 5);
        System.out.println("\nAttempt to add mechanical product to electronics line:");
        if (!eLine.canProduce(wrong)) {
            System.out.println("  -> eLine.canProduce(wrong) == false, skipping add (as expected).");
        }

        System.out.println("\nTesting unmodifiable products view for eLine:");
        List<ElectronicProduct> eProductsView = eLine.getProducts();
        System.out.println("  eLine products before attempted modification: " + names(eProductsView));
        try {
            eProductsView.add(new ElectronicProduct("E999", "Illegal", 1));
            System.out.println("  <-- unexpected: was able to modify products view");
        } catch (UnsupportedOperationException ex) {
            System.out.println("  -> cannot modify products view directly (UnsupportedOperationException)");
        }

        System.out.println("\nTesting efficiency validation:");
        try {
            System.out.println("  Current eLine efficiency: " + eLine.getEfficiency());
            eLine.setEfficiency(1.5);
        } catch (IllegalArgumentException iae) {
            System.out.println("  -> caught IllegalArgumentException when setting invalid efficiency: " + iae.getMessage());
        }

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(eLine);
        analyzer.addLine(mLine);
        analyzer.addLine(cLine);

        System.out.println("\n--- All lines and their products ---");
        analyzer.getAllLines().forEach(line -> {
            System.out.printf("Line %s (eff=%.2f) - products=%d\n",
                    line.getLineId(), line.getEfficiency(), line.getProducts().size());
            line.getProducts().forEach(p -> System.out.printf("   - %s | id=%s | prodTime=%d | cat=%s\n",
                    p.getName(), p.getId(), p.getProductionTime(), p.getCategory()));
        });

        System.out.println("\nHigh-eff lines (> 0.8): " + analyzer.getHighEfficiencyLines(0.8));
        System.out.println("High-eff lines (> 0.65): " + analyzer.getHighEfficiencyLines(0.65));

        System.out.println("\nCounts by category:");
        Map<String, Long> counts = analyzer.countProductsByCategory();
        counts.forEach((cat, cnt) -> System.out.printf("  %s -> %d\n", cat, cnt));

        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        System.out.println("\nMost loaded line: " + mostLoaded.map(l -> l.getLineId() + " (count=" + l.getProducts().size() + ")").orElse("none"));

        System.out.println("\nAll products collected from all lines:");
        List<Product> allProducts = analyzer.getAllProductsFromLines();
        allProducts.forEach(p -> System.out.printf("  - %s | id=%s | cat=%s | time=%d\n",
                p.getName(), p.getId(), p.getCategory(), p.getProductionTime()));

        System.out.println("\nRemoving product " + m2.getName() + " from " + mLine.getLineId());
        boolean removed = mLine.removeProduct(m2);
        System.out.println("  removed? " + removed);
        System.out.println("Counts by category after removal:");
        analyzer.countProductsByCategory().forEach((cat, cnt) -> System.out.printf("  %s -> %d\n", cat, cnt));

        System.out.println("\n=== Summary ===");
        System.out.println("Total lines: " + analyzer.getAllLines().size());
        System.out.println("Total products across all lines: " + analyzer.getAllProductsFromLines().size());
    }

    private static <T extends Product> void addIfCan(ProductionLine<T> line, Product product) {
        if (line.canProduce(product)) {
            @SuppressWarnings("unchecked")
            T casted = (T) product;   // безопасно, потому что мы проверили canProduce()
            line.addProduct(casted);
            System.out.printf("Added product %s to line %s%n", product.getName(), line.getLineId());
        } else {
            System.out.printf("Cannot add product %s to line %s (type mismatch)%n", product.getName(), line.getLineId());
        }
    }

    private static String names(List<? extends Product> products) {
        return products.stream().map(Product::getName).toList().toString();
    }
}
