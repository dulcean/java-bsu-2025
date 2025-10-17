package lab1;

import lab1.model.line.ChemicalLine;
import lab1.model.line.ElectronicsLine;
import lab1.model.line.MechanicalLine;
import lab1.model.line.ProductionLine;
import lab1.model.product.ChemicalProduct;
import lab1.model.product.ElectronicProduct;
import lab1.model.product.MechanicalProduct;
import lab1.service.ProdAnalyzer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var analyzer = getProdAnalyzer();

        System.out.println("======= Production Lines Analyzer =======");
        System.out.println("High efficiency lines(> 0.8): " + analyzer.getHighEfficiencyLines(0.8));
        System.out.println("Count products by category: " + analyzer.countProductsByCategory());
        System.out.println("Most loaded line: " + analyzer.findMostLoadedLine().map(ProductionLine::getLineId));
        System.out.println("Total products: " + analyzer.getAllProductsFromLines().size());
        System.out.println("Total time: " + analyzer.calculateTotalProductionTime());
    }

    private static ProdAnalyzer getProdAnalyzer() {
        var e1 = new ElectronicProduct("E1", "POCO X3 PRO", 20);
        var e2 = new ElectronicProduct("E2", "jPhone 23", 40);
        var m1 = new MechanicalProduct("M1", "Factorio Gear",  42);
        var c1 = new ChemicalProduct("C1", "Chemical Science Pack", 67);

        var el = new ElectronicsLine("EL-1", 0.85);
        el.addProduct(e1);
        el.addProduct(e2);

        var ml = new MechanicalLine("ML-1", 0.67);
        ml.addProduct(m1);

        var cl = new ChemicalLine("CL-1", 0.42);
        cl.addProduct(c1);

        return new ProdAnalyzer(List.of(el, ml, cl));
    }
}
