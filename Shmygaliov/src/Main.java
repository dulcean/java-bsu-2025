import production.model.*;
import production.line.*;
import production.analyzer.ProdAnalyzer;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ElectronicProduct phone = new ElectronicProduct("E1", "Phone", 120);
        ElectronicProduct laptop = new ElectronicProduct("E2", "Laptop", 240);
        MechanicalProduct gear = new MechanicalProduct("M1", "Gear", 180);
        ChemicalProduct pivo = new ChemicalProduct("C1", "Pivo", 90);

        ElectronicsLine eLine = new ElectronicsLine("EL-01", 0.9);
        MechanicalLine mLine = new MechanicalLine("ML-01", 0.75);
        ChemicalLine cLine = new ChemicalLine("CL-01", 0.8);

        eLine.addProduct(phone);
        eLine.addProduct(laptop);
        mLine.addProduct(gear);
        cLine.addProduct(pivo);

        ProdAnalyzer analyzer = new ProdAnalyzer(List.of(eLine, mLine, cLine));

        System.out.println("Высокоэффективные линии: " + analyzer.getHighEfficiencyLines(0.8));
        System.out.println("Подсчёт по категориям: " + analyzer.countProductsByCategory());
        analyzer.findMostLoadedLine().ifPresent(line ->
                System.out.println("Самая загруженная линия: " + line.getLineId()));
        System.out.println("Общее время производства: " + analyzer.calculateTotalProductionTime() + " минут");
    }
}
