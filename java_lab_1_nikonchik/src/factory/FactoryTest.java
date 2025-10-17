package factory;

import bsu.fpmi.nikonchik.javalabs.lab1.analysis.ProdAnalyzer;
import bsu.fpmi.nikonchik.javalabs.lab1.productionlines.ProductionLine;
import bsu.fpmi.nikonchik.javalabs.lab1.products.ChemicalProduct;
import bsu.fpmi.nikonchik.javalabs.lab1.products.ElectronicProduct;
import bsu.fpmi.nikonchik.javalabs.lab1.products.MechanicalProduct;
import bsu.fpmi.nikonchik.javalabs.lab1.products.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FactoryTest {
    public static void main(String[] args) {
        System.out.println("---Тестирование системы анализа производственных линий---\n");

        // Создаем тестовые продукты
        ChemicalProduct chem1 = new ChemicalProduct("CH1", "Аммиак", 120);
        ChemicalProduct chem2 = new ChemicalProduct("CH2", "Серная кислота", 180);
        ElectronicProduct elec1 = new ElectronicProduct("EL1", "Смартфон", 300);
        ElectronicProduct elec2 = new ElectronicProduct("EL2", "Ноутбук", 600);
        ElectronicProduct elec3 = new ElectronicProduct("EL3", "Планшет", 400);
        MechanicalProduct mech1 = new MechanicalProduct("MEC1", "Подшипник", 90);
        MechanicalProduct mech2 = new MechanicalProduct("MEC2", "Шестерня", 75);

        // Создаем производственные линии
        ProductionLine<ChemicalProduct> chemLine = new ProductionLine<>(
                "CHEM_LINE_1", 0.85, ChemicalProduct.class, List.of(chem1, chem2)
        );

        ProductionLine<ElectronicProduct> elecLine = new ProductionLine<>(
                "ELEC_LINE_1", 0.92, ElectronicProduct.class, List.of(elec1, elec2, elec3)
        );

        ProductionLine<MechanicalProduct> mechLine = new ProductionLine<>(
                "MECH_LINE_1", 0.78, MechanicalProduct.class, List.of(mech1, mech2)
        );

        System.out.println("Тест 1: создание ProdAnalyzer");
        ProdAnalyzer analyzer = new ProdAnalyzer(chemLine, elecLine, mechLine);
        System.out.println("Количество линий: " + analyzer.getAllLines().size());
        System.out.println("Ожидалось: 3");
        System.out.println("Результат: " + (analyzer.getAllLines().size() == 3 ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        System.out.println();

        System.out.println("Тест 2: подсчет продуктов по категориям");
        Map<String, Long> categoryCount = analyzer.countProductsByCategory();
        System.out.println("Результат подсчета: " + categoryCount);
        System.out.println("Ожидалось: Chemical=2, Electronics=3, Mechanical=2");
        boolean categoryTest = categoryCount.get("Chemical") == 2 &&
                categoryCount.get("Electronics") == 3 &&
                categoryCount.get("Mechanical") == 2;
        System.out.println("Результат: " + (categoryTest ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        System.out.println();

        System.out.println("Тест 3: поиск высокоэффективных линий");
        List<String> highEffLines = analyzer.getHighEfficiencyLines(0.80);
        System.out.println("Линии с эффективностью > 0.80: " + highEffLines);
        System.out.println("Ожидалось: [CHEM_LINE_1, ELEC_LINE_1]");
        boolean efficiencyTest = highEffLines.size() == 2 &&
                highEffLines.contains("CHEM_LINE_1") &&
                highEffLines.contains("ELEC_LINE_1");
        System.out.println("Результат: " + (efficiencyTest ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        System.out.println();

        System.out.println("Тест 4: поиск самой загруженной линии");
        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        if (mostLoaded.isPresent()) {
            System.out.println("Самая загруженная линия: " + mostLoaded.get().getLineId());
            System.out.println("Количество продуктов: " + mostLoaded.get().countProducts());
            System.out.println("Ожидалось: ELEC_LINE_1 с 3 продуктами");
            boolean loadedTest = mostLoaded.get().getLineId().equals("ELEC_LINE_1") &&
                    mostLoaded.get().countProducts() == 3;
            System.out.println("Результат: " + (loadedTest ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        } else {
            System.out.println("Результат: НЕ ПРОЙДЕН (линия не найдена)");
        }
        System.out.println();

        System.out.println("Тест 5: расчет общего времени производства");
        double totalTime = analyzer.calculateTotalProductionTimeInMinutes();
        System.out.println(String.format("Общее время производства (минуты): %.2f", totalTime));

        //Расчет: (120+180+300+600+400+90+75) / 60 = 1765 / 60 = 29.416...
        boolean timeTest = Math.abs(totalTime - 29.416) < 0.1;
        System.out.println("Ожидалось: ~29.42 минут");
        System.out.println("Результат: " + (timeTest ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        System.out.println();

        System.out.println("Тест 6: получение всех продуктов");
        List<? extends Product> allProducts = analyzer.getAllProductsFromLines();
        System.out.println("Общее количество продуктов: " + allProducts.size());
        System.out.println("Ожидалось: 7");
        System.out.println("Результат: " + (allProducts.size() == 7 ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        System.out.println();

        System.out.println("Тест 7: добавление продуктов на линию");
        try {
            ElectronicProduct elec4 = new ElectronicProduct("EL4", "Наушники", 150);
            elecLine.addProducts(elec4);
            System.out.println("Продукт успешно добавлен. Теперь продуктов на линии: " + elecLine.countProducts());
            System.out.println("Ожидалось: 4");
            System.out.println("Результат: " + (elecLine.countProducts() == 4 ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
            System.out.println("Результат: НЕ ПРОЙДЕН");
        }
        System.out.println();

        System.out.println("Тест 8: попытка добавить неправильный тип продукта");
        try {
            chemLine.addProducts(elec1); // Пытаемся добавить электронный продукт на химическую линию
            System.out.println("Результат: НЕ ПРОЙДЕН (должна быть ошибка)");
        } catch (IllegalArgumentException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
            System.out.println("Результат: ПРОЙДЕН");
        }
        System.out.println();

        System.out.println("Тест 9: работа с пустым анализатором");
        ProdAnalyzer emptyAnalyzer = new ProdAnalyzer();
        Optional<ProductionLine<? extends Product>> emptyMostLoaded = emptyAnalyzer.findMostLoadedLine();
        System.out.println("Поиск в пустом анализаторе: " + emptyMostLoaded);
        System.out.println("Ожидалось: Optional.empty");
        System.out.println("Результат: " + (emptyMostLoaded.isEmpty() ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН"));
    }
}