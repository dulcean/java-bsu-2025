package main.java.com.production.app;

import main.java.com.production.analyzer.ProdAnalyzer;
import main.java.com.production.model.line.ChemicalLine;
import main.java.com.production.model.line.ElectronicsLine;
import main.java.com.production.model.line.MechanicalLine;
import main.java.com.production.model.line.ProductionLine;
import main.java.com.production.model.product.ChemicalProduct;
import main.java.com.production.model.product.ElectronicProduct;
import main.java.com.production.model.product.MechanicalProduct;
import main.java.com.production.model.product.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductionApp {

    public static void main(String[] args) {
        ElectronicsLine elLine1 = new ElectronicsLine("EL-101", 0.95);
        ElectronicsLine elLine2 = new ElectronicsLine("EL-102", 0.65);
        MechanicalLine mechLine = new MechanicalLine("ME-201", 0.88);
        ChemicalLine chemLine = new ChemicalLine("CH-301", 0.99);

        ElectronicProduct phone = new ElectronicProduct("P001", "Smartphone", 180);
        ElectronicProduct tablet = new ElectronicProduct("P002", "Tablet", 250);
        ElectronicProduct laptop = new ElectronicProduct("P003", "Laptop", 350);

        MechanicalProduct gear = new MechanicalProduct("G001", "Gear", 60);
        MechanicalProduct engine = new MechanicalProduct("E001", "Engine", 1200);

        ChemicalProduct oil = new ChemicalProduct("O001", "Motor Oil", 15);
        ChemicalProduct solvent = new ChemicalProduct("S001", "Solvent", 20);

        try {
            elLine1.addProduct(phone);
            elLine1.addProduct(tablet);
            elLine2.addProduct(phone);
            elLine2.addProduct(phone);
            elLine2.addProduct(phone);
            mechLine.addProduct(gear);
            mechLine.addProduct(engine);
            chemLine.addProduct(oil);
            chemLine.addProduct(solvent);
        } catch (IllegalArgumentException e) {
            System.err.println("ОШИБКА ПРИ ДОБАВЛЕНИИ: " + e.getMessage());
        }

        System.out.println("--- Тест: Проверка несовместимости и валидации ---");

        try {
            elLine1.addProduct(oil);
        } catch (IllegalArgumentException e) {
            System.out.println("✅ УСПЕХ (Несовместимость): " + e.getMessage());
        }

        try {
            elLine1.addProduct(laptop);
        } catch (IllegalArgumentException e) {
            System.out.println("✅ УСПЕХ (Валидация): " + e.getMessage());
        }

        List<ProductionLine<? extends Product>> allLines = Arrays.asList(elLine1, elLine2, mechLine, chemLine);
        ProdAnalyzer analyzer = new ProdAnalyzer(allLines);

        System.out.println("\n--- Результаты Анализа (Stream API) ---");

        double threshold = 0.90;
        List<String> highEfficiencyLines = analyzer.getHighEfficiencyLines(threshold);
        System.out.printf("Lines with efficiency > %.2f: %s\n", threshold, highEfficiencyLines);

        Map<String, Long> counts = analyzer.countProductsByCategory();
        System.out.println("Products by Category: " + counts);

        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        mostLoaded.ifPresent(line ->
                System.out.println("Most Loaded Line: " + line.getLineId() +
                        " with " + line.getProducts().size() + " products")
        );

        double totalTime = analyzer.calculateTotalProductionTime();
        System.out.printf("ADVANCED: Total Production Time: %.0f minutes\n", totalTime);
    }
}
