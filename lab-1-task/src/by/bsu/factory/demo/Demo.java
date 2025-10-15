package by.bsu.factory.demo;

import by.bsu.factory.line.ChemicalLine;
import by.bsu.factory.line.ElectronicsLine;
import by.bsu.factory.line.MechanicalLine;
import by.bsu.factory.line.ProductionLine;
import by.bsu.factory.product.ChemicalProduct;
import by.bsu.factory.product.ElectronicProduct;
import by.bsu.factory.product.MechanicalProduct;
import by.bsu.factory.product.Product;
import by.bsu.factory.service.ProdAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Demo {
    public static void main(String[] args) {
        ElectronicsLine el = new ElectronicsLine("EL-01", new ArrayList<>(), 0.92);
        MechanicalLine ml = new MechanicalLine("ML-02", new ArrayList<>(), 0.78);
        ChemicalLine cl = new ChemicalLine("CL-03", new ArrayList<>(), 0.85);

        el.addProduct(new ElectronicProduct("E-1", "Sensor", 40));
        el.addProduct(new ElectronicProduct("E-2", "Controller", 55));
        ml.addProduct(new MechanicalProduct("M-1", "Gear", 30));
        ml.addProduct(new MechanicalProduct("M-2", "Shaft", 70));
        cl.addProduct(new ChemicalProduct("C-1", "Reagent", 120));

        List<ProductionLine<? extends Product>> lines = List.of(el, ml, cl);
        ProdAnalyzer analyzer = new ProdAnalyzer(lines);

        List<ProductionLine<? extends Product>> allLines = analyzer.getAllLines();
        List<String> highEff = analyzer.getHighEfficiencyLines(0.80);
        Map<String, Long> byCategory = analyzer.countProductsByCategory();
        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        List<Product> allProducts = analyzer.getAllProductsFromLines();
        double totalTime = analyzer.calculateTotalProductionTime();

        System.out.println(allLines.size());
        System.out.println(highEff);
        System.out.println(byCategory);
        System.out.println(mostLoaded.map(ProductionLine::getLineId).orElse(""));
        System.out.println(allProducts.size());
        System.out.println(totalTime);
    }
}