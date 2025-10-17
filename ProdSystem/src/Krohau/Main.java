package Krohau;

import Krohau.model.*;
import Krohau.service.ProdAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ElectronicProduct smartPhone = new ElectronicProduct("E001", "Смартфон", 60);
        ElectronicProduct tab = new ElectronicProduct("E002", "Планшет", 90);
        MechanicalProduct bolt = new MechanicalProduct("M001", "Болт", 30);
        MechanicalProduct nut = new MechanicalProduct("M002", "Гайка", 45);
        ChemicalProduct chemA = new ChemicalProduct("C001", "Вещество А", 120);
        ChemicalProduct chemB = new ChemicalProduct("C002", "Вещество Б", 100);

        ElectronicLine el1 = new ElectronicLine("ЭЛ01", 0.95);
        el1.addProduct(smartPhone);
        el1.addProduct(tab);
        el1.addProduct(smartPhone);

        ElectronicLine el2 = new ElectronicLine("ЭЛ02", 0.70);
        el2.addProduct(tab);

        MechanicalLine mLine1 = new MechanicalLine("МХ01", 0.85);
        mLine1.addProduct(bolt);
        mLine1.addProduct(nut);

        ChemicalLine chL1 = new ChemicalLine("ХМ01", 0.90);
        chL1.addProduct(chemA);
        chL1.addProduct(chemB);
        chL1.addProduct(chemA);

        List<ProductionLine<? extends Product>> allLs = new ArrayList<>();
        allLs.add(el1);
        allLs.add(el2);
        allLs.add(mLine1);
        allLs.add(chL1);

        ProdAnalyzer anal = new ProdAnalyzer(allLs);

        System.out.println("Проверка рабты");

        System.out.println("\nПровекрка совместимости");
        System.out.println("Электронная линия ЭЛ01 может производить смартфон? " + el1.canProduce(smartPhone)); // true
        System.out.println("Электронная линия ЭЛ01 может производить планшет? " + el1.canProduce(tab));        // true
        System.out.println("Электронная линия ЭЛ01 может производить болт? " + el1.canProduce(bolt));           // false
        System.out.println("Электронная линия ЭЛ01 может производить вещество А? " + el1.canProduce(chemA));  // false

        System.out.println("Механическая линия МХ01 может производить болт? " + mLine1.canProduce(bolt));     // true
        System.out.println("Механическая линия МХ01 может производить смартфон? " + mLine1.canProduce(smartPhone)); // false

        System.out.println("\nЭффективные линии (> 0.8):");
        List<String> goodLines = anal.getHighEfficiencyLines(0.8);
        goodLines.forEach(System.out::println);

        System.out.println("\nСколько чего производится:");
        Map<String, Long> byCat = anal.countProductsByCategory();
        byCat.forEach((cat, cnt) -> System.out.println(cat + ": " + cnt));

        System.out.println("\nСамая загруженная линия:");
        Optional<ProductionLine<? extends Product>> maxLine = anal.findMostLoadedLine();
        maxLine.ifPresent(line -> System.out.println("ID: " + line.getLineId() + ", Продуктов: " + line.getProducts().size()));

        System.out.println("\nВсе продукты со всех линий:");
        List<Product> allProds = anal.getAllProductsFromLines();
        allProds.forEach(p -> System.out.println(" - " + p.getName() + " (" + p.getCategory() + ")"));

        System.out.println("\nОбщее время производства (минут):");
        int totalTime = anal.calculateTotalProductionTime();
        System.out.println(totalTime + " мин.");

    }
}