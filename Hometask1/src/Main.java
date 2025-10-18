import com.production.ProdAnalyzer;
import com.production.lines.ChemicalLine;
import com.production.lines.ElectronicsLine;
import com.production.lines.MechanicalLine;
import com.production.lines.ProductionLine;
import com.production.products.ChemicalProduct;
import com.production.products.ElectronicProduct;
import com.production.products.MechanicalProduct;
import com.production.products.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        ElectronicProduct phone = new ElectronicProduct("E001", "Smartphone X", 120);
        ElectronicProduct tablet = new ElectronicProduct("E002", "Tablet Pro", 180);

        MechanicalProduct engine = new MechanicalProduct("M001", "V8 Engine", 720);
        MechanicalProduct gearbox = new MechanicalProduct("M002", "6-Speed Gearbox", 450);
        MechanicalProduct pump = new MechanicalProduct("M003", "Water Pump", 200);

        ChemicalProduct plastic = new ChemicalProduct("C001", "Polymer Pellets", 300);
        ChemicalProduct fertilizer = new ChemicalProduct("C002", "Nitrogen Fertilizer", 400);

        List<ElectronicProduct> electronicProducts = List.of(phone, tablet);
        ElectronicsLine electronicsLine = new ElectronicsLine("EL-LINE-01", electronicProducts, 0.95);

        List<MechanicalProduct> mechanicalProducts = List.of(engine, gearbox, pump);
        MechanicalLine mechanicalLine = new MechanicalLine("MECH-LINE-01", mechanicalProducts, 0.90);

        List<ChemicalProduct> chemicalProducts = List.of(plastic, fertilizer);
        ChemicalLine chemicalLine = new ChemicalLine("CHEM-LINE-01", chemicalProducts, 0.98);

        System.out.println(phone.getInfo());

        System.out.println("Анализ производства");

        ProdAnalyzer pa = new ProdAnalyzer(List.of(electronicsLine, mechanicalLine, chemicalLine));

        List<String> hef = pa.getHighEfficiencyLines(0.97);
        System.out.println("Самая эффективная линия");
        for (String line : hef) {System.out.println(line);}

        Map<String, Long> m = pa.countProductsByCategory();
        System.out.println("Количество продуктов в линиях");
        System.out.println(m);

        Optional<ProductionLine<? extends Product>> mll = pa.findMostLoadedLine();
        System.out.println("Самая загруженная линия");
        System.out.println(mll);

        double totalTime = pa.calculateTotalProductionTime();
        System.out.println("Общее время производства всех изделий: " + totalTime + " минут.");

        System.out.println("Попытка добавить 'V8 Engine' на линию электроники...");
        try {
            electronicsLine.addProduct(engine);
        } catch (IllegalArgumentException e) {
            // Мы ожидаем это исключение, поэтому ловим его и красиво выводим сообщение
            System.err.println("ОШИБКА: " + e.getMessage());
        }
    }
}
