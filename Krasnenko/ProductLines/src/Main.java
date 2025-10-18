
import ProdAnalyzer.ProdAnalyzer;
import ProductLine.*;
import Product.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ElectronicsLine eLine = new ElectronicsLine("E-01", 0.9);
        MechanicalLine mLine = new MechanicalLine("M-01", 0.75);
        ChemicalLine cLine = new ChemicalLine("C-01", 0.8);

        eLine.addProduct(new ElectronicProduct("P1", "Smartphone", 120));
        eLine.addProduct(new ElectronicProduct("P2", "Tablet", 150));
        mLine.addProduct(new MechanicalProduct("P3", "Gear", 200));
        cLine.addProduct(new ChemicalProduct("P4", "Paint", 100));
        cLine.addProduct(new ChemicalProduct("P5", "Solvent", 80));

        ProdAnalyzer analyzer = new ProdAnalyzer(List.of(eLine, mLine, cLine));

        System.out.println("High efficiency lines (>0.8): " + analyzer.getHighEfficiencyLines(0.8));
        System.out.println("Products by category: " + analyzer.countProductsByCategory());
        System.out.println("Most loaded line: " + analyzer.findMostLoadedLine().map(ProductionLine::getLineId).orElse("None"));
        System.out.println("Total production time: " + analyzer.calculateTotalProductionTime());
    }
}