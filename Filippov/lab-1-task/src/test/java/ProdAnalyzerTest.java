package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProdAnalyzerTest {

    private ElectronicsLine e1;
    private ElectronicsLine e2;
    private MechanicalLine m1;
    private ChemicalLine c1;
    private ProdAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        e1 = new ElectronicsLine("E-100", 0.85);
        e2 = new ElectronicsLine("E-200", 0.60);
        m1 = new MechanicalLine("M-100", 0.92);
        c1 = new ChemicalLine("C-50", 0.55);

        e1.addProduct(new ElectronicProduct("EP-01", "SmartSensor", 30));
        e1.addProduct(new ElectronicProduct("EP-02", "ControlBoard", 45));

        e2.addProduct(new ElectronicProduct("EP-03", "BatteryPack", 20));
        e2.addProduct(new ElectronicProduct("EP-04", "Display", 25));

        m1.addProduct(new MechanicalProduct("MP-01", "GearBox", 120));
        m1.addProduct(new MechanicalProduct("MP-02", "Shaft", 60));
        m1.addProduct(new MechanicalProduct("MP-03", "Bearing", 10));

        c1.addProduct(new ChemicalProduct("CP-01", "PolymerBatch", 240));

        analyzer = new ProdAnalyzer();
        analyzer.registerLine(e1);
        analyzer.registerLine(e2);
        analyzer.registerLine(m1);
        analyzer.registerLine(c1);
    }

    @Test
    void getAllLinesReturnsRegisteredLines() {
        List<ProductionLine<? extends Product>> lines = analyzer.getAllLines();
        assertEquals(4, lines.size());
        assertTrue(lines.stream().anyMatch(l -> l.getLineId().equals("E-100")));
    }

    @Test
    void getHighEfficiencyLinesWorks() {
        List<String> high = analyzer.getHighEfficiencyLines(0.8);
        assertTrue(high.contains("E-100"));
        assertTrue(high.contains("M-100"));
        assertFalse(high.contains("C-50"));
    }

    @Test
    void countProductsByCategoryCorrect() {
        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(3L, counts.get("Mechanical"));
        assertEquals(4L, counts.get("Electronics"));
        assertEquals(1L, counts.get("Chemical"));
    }

    @Test
    void findMostLoadedLineReturnsMechanicalLine() {
        Optional<ProductionLine<? extends Product>> most = analyzer.findMostLoadedLine();
        assertTrue(most.isPresent());
        assertEquals("M-100", most.get().getLineId());
    }

    @Test
    void getAllProductsFromLinesContainsEverythingAndTotalTime() {
        List<Product> all = analyzer.getAllProductsFromLines();
        assertEquals(8, all.size());
        double total = analyzer.calculateTotalProductionTime();
        assertEquals(550.0, total);
    }

    @Test
    void registerLineRejectsNull() {
        assertThrows(NullPointerException.class, () -> analyzer.registerLine(null));
    }
}
