package com.enterprise.main;

import com.enterprise.model.lines.ChemicalLine;
import com.enterprise.model.lines.ElectronicsLine;
import com.enterprise.model.lines.MechanicalLine;
import com.enterprise.model.lines.ProductionLine;
import com.enterprise.model.products.ChemicalProduct;
import com.enterprise.model.products.ElectronicProduct;
import com.enterprise.model.products.MechanicalProduct;
import com.enterprise.model.products.Product;
import com.enterprise.service.ProdAnalyzer;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ElectronicsLine elLine1 = new ElectronicsLine("EL-001", 0.95);
        MechanicalLine mechLine1 = new MechanicalLine("MECH-001", 0.88);
        ChemicalLine chemLine1 = new ChemicalLine("CHEM-001", 0.92);

        elLine1.addProduct(new ElectronicProduct("E01", "Smartphone", 120));
        elLine1.addProduct(new ElectronicProduct("E02", "Laptop", 180));
        mechLine1.addProduct(new MechanicalProduct("M01", "Gearbox", 300));
        chemLine1.addProduct(new ChemicalProduct("C01", "Polymer", 240));
        chemLine1.addProduct(new ChemicalProduct("C02", "Fertilizer", 260));

        try {
            mechLine1.addProduct(new ElectronicProduct("E03", "Tablet", 150));
        } catch (IllegalArgumentException e) {
            System.out.println("Validation test passed: " + e.getMessage());
        }

        List<ProductionLine<? extends Product>> allLines = List.of(elLine1, mechLine1, chemLine1);
        ProdAnalyzer analyzer = new ProdAnalyzer(allLines);

        System.out.println("High efficiency lines (>0.9): " + analyzer.getHighEfficiencyLines(0.9));
        System.out.println("Products by category: " + analyzer.countProductsByCategory());

        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        mostLoaded.ifPresent(line -> System.out.println("Most loaded line: " + line.getLineId()));

        System.out.println("All products from all lines: " + analyzer.getAllProductsFromLines().size());
        System.out.println("Total production time: " + analyzer.calculateTotalProductionTime());
    }
}
