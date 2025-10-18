package com.bsu.production.analyzer;

import com.bsu.production.line.*;
import com.bsu.production.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdAnalyzerTest {
    @Test
    void testCanProduce() {
        ElectronicsLine el = new ElectronicsLine("E1", 0.9);
        assertTrue(el.canProduce(new ElectronicProduct("e","p",1)));
        assertFalse(el.canProduce(new MechanicalProduct("m","p",1)));
    }

    @Test
    void testCountProductsByCategoryAndHighEff() {
        ElectronicsLine el = new ElectronicsLine("E1", 0.95);
        el.addProduct(new ElectronicProduct("E1-1","A",1));

        MechanicalLine ml = new MechanicalLine("M1", 0.5);
        ml.addProduct(new MechanicalProduct("M1-1","B",2));
        ml.addProduct(new MechanicalProduct("M1-2","C",3));

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(el);
        analyzer.addLine(ml);

        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(1L, counts.get("Electronics"));
        assertEquals(2L, counts.get("Mechanical"));

        assertEquals(1, analyzer.getHighEfficiencyLines(0.9).size());
    }

    @Test
    void testFindMostLoadedLine() {
        MechanicalLine ml = new MechanicalLine("M1", 0.6);
        ml.addProduct(new MechanicalProduct("m1","a",1));
        ml.addProduct(new MechanicalProduct("m2","b",1));
        ElectronicsLine el = new ElectronicsLine("E1", 0.8);
        el.addProduct(new ElectronicProduct("e1","a",1));

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(ml); analyzer.addLine(el);

        Optional<ProductionLine<? extends Product>> most = analyzer.findMostLoadedLine();
        assertTrue(most.isPresent());
        assertEquals("M1", most.get().getLineId());
    }
}
