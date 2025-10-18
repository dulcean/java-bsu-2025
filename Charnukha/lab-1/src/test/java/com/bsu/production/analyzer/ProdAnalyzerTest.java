package com.bsu.production.analyzer;

import com.bsu.production.line.ChemicalLine;
import com.bsu.production.line.ElectronicsLine;
import com.bsu.production.line.MechanicalLine;
import com.bsu.production.line.ProductionLine;
import com.bsu.production.model.ChemicalProduct;
import com.bsu.production.model.ElectronicProduct;
import com.bsu.production.model.MechanicalProduct;
import com.bsu.production.model.Product;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductionModuleTest {

    @Test
    void testProductCategoriesAndToString() {
        Product e = new ElectronicProduct("E1", "Phone", 10);
        Product m = new MechanicalProduct("M1", "Gear", 5);
        Product c = new ChemicalProduct("C1", "Coating", 120);

        assertEquals("Electronics", e.getCategory());
        assertEquals("Mechanical", m.getCategory());
        assertEquals("Chemical", c.getCategory());

        assertTrue(e.toString().contains("Electronics"));
        assertTrue(e.toString().contains("E1"));
    }

    @Test
    void testProductionLineAddRemoveAndUnmodifiableProducts() {
        ElectronicsLine eLine = new ElectronicsLine("EL-1", 0.75);
        ElectronicProduct p1 = new ElectronicProduct("E-101", "Board", 30);
        ElectronicProduct p2 = new ElectronicProduct("E-102", "Module", 20);

        assertEquals("EL-1", eLine.getLineId());
        assertEquals(0.75, eLine.getEfficiency());

        eLine.addProduct(p1);
        eLine.addProduct(p2);

        List<ElectronicProduct> productsView = eLine.getProducts();
        assertEquals(2, productsView.size());
        assertTrue(productsView.contains(p1));
        assertTrue(productsView.contains(p2));

        boolean removed = eLine.removeProduct(p1);
        assertTrue(removed);
        assertEquals(1, eLine.getProducts().size());

        assertThrows(UnsupportedOperationException.class, () -> productsView.add(new ElectronicProduct("E-103", "X", 1)));
    }

    @Test
    void testProductionLineEfficiencyValidation() {
        MechanicalLine mLine = new MechanicalLine("ML-1", 0.5);
        assertEquals(0.5, mLine.getEfficiency());

        mLine.setEfficiency(1.0);
        assertEquals(1.0, mLine.getEfficiency());

        assertThrows(IllegalArgumentException.class, () -> mLine.setEfficiency(-0.1));
        assertThrows(IllegalArgumentException.class, () -> mLine.setEfficiency(1.0001));
    }

    @Test
    void testCanProduceForConcreteLines() {
        ElectronicsLine el = new ElectronicsLine("E1", 0.9);
        MechanicalLine ml = new MechanicalLine("M1", 0.8);
        ChemicalLine cl = new ChemicalLine("C1", 0.7);

        ElectronicProduct ep = new ElectronicProduct("E1-1", "Phone", 10);
        MechanicalProduct mp = new MechanicalProduct("M1-1", "Gear", 7);
        ChemicalProduct cp = new ChemicalProduct("C1-1", "Coating", 120);

        assertTrue(el.canProduce(ep));
        assertFalse(el.canProduce(mp));
        assertFalse(el.canProduce(cp));

        assertTrue(ml.canProduce(mp));
        assertFalse(ml.canProduce(ep));
        assertFalse(ml.canProduce(cp));

        assertTrue(cl.canProduce(cp));
        assertFalse(cl.canProduce(ep));
        assertFalse(cl.canProduce(mp));
    }

    @Test
    void testProdAnalyzerAllFunctionsTogether() {
        ElectronicsLine eLine = new ElectronicsLine("E-01", 0.95);
        eLine.addProduct(new ElectronicProduct("E100", "PhoneBoard", 30));
        eLine.addProduct(new ElectronicProduct("E101", "CameraModule", 20));

        MechanicalLine mLine = new MechanicalLine("M-01", 0.7);
        mLine.addProduct(new MechanicalProduct("M200", "Gear", 15));
        mLine.addProduct(new MechanicalProduct("M201", "Shaft", 10));
        mLine.addProduct(new MechanicalProduct("M202", "Bracket", 8));

        ChemicalLine cLine = new ChemicalLine("C-01", 0.6);
        cLine.addProduct(new ChemicalProduct("C300", "Coating", 120));

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(eLine);
        analyzer.addLine(mLine);
        analyzer.addLine(cLine);

        List<ProductionLine<? extends Product>> all = analyzer.getAllLines();
        assertEquals(3, all.size());
        assertTrue(all.stream().anyMatch(l -> l.getLineId().equals("E-01")));

        List<String> high = analyzer.getHighEfficiencyLines(0.8);
        assertEquals(1, high.size());
        assertTrue(high.contains("E-01"));

        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(2L, counts.get("Electronics"));
        assertEquals(3L, counts.get("Mechanical"));
        assertEquals(1L, counts.get("Chemical"));

        Optional<ProductionLine<? extends Product>> most = analyzer.findMostLoadedLine();
        assertTrue(most.isPresent());
        assertEquals("M-01", most.get().getLineId());

        List<Product> allProducts = analyzer.getAllProductsFromLines();
        assertEquals(6, allProducts.size());
        assertTrue(allProducts.stream().anyMatch(p -> p.getName().equals("PhoneBoard")));
    }

    @Test
    void testRemoveProductReflectsInAnalyzerCounts() {
        ElectronicsLine eLine = new ElectronicsLine("E-EL", 0.9);
        ElectronicProduct p = new ElectronicProduct("E-1", "Phone", 10);
        eLine.addProduct(p);

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(eLine);

        Map<String, Long> before = analyzer.countProductsByCategory();
        assertEquals(1L, before.get("Electronics"));

        boolean removed = eLine.removeProduct(p);
        assertTrue(removed);

        Map<String, Long> after = analyzer.countProductsByCategory();
        assertEquals(0L, (long) after.getOrDefault("Electronics", 0L));
    }
}
