package com.bsu.production.app;

import com.bsu.production.analyzer.ProdAnalyzer;
import com.bsu.production.line.*;
import com.bsu.production.model.*;

import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ElectronicsLine eLine = new ElectronicsLine("E-01", 0.95);
        MechanicalLine mLine = new MechanicalLine("M-01", 0.7);
        ChemicalLine cLine = new ChemicalLine("C-01", 0.6);

        eLine.addProduct(new ElectronicProduct("E100", "PhoneBoard", 30));
        eLine.addProduct(new ElectronicProduct("E101", "CameraModule", 20));

        mLine.addProduct(new MechanicalProduct("M200", "Gear", 15));
        mLine.addProduct(new MechanicalProduct("M201", "Shaft", 10));
        mLine.addProduct(new MechanicalProduct("M202", "Bracket", 8));

        cLine.addProduct(new ChemicalProduct("C300", "Coating", 120));

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(eLine);
        analyzer.addLine(mLine);
        analyzer.addLine(cLine);

        System.out.println("All lines:");
        analyzer.getAllLines().forEach(l -> System.out.println(" - " + l.getLineId()));

        System.out.println("High-eff lines (>0.8): " + analyzer.getHighEfficiencyLines(0.8));

        Map<String, Long> byCat = analyzer.countProductsByCategory();
        System.out.println("Counts by category: " + byCat);

        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        System.out.println("Most loaded: " + mostLoaded.map(ProductionLine::getLineId).orElse("none"));

        System.out.println("All products: " + analyzer.getAllProductsFromLines());
    }
}
