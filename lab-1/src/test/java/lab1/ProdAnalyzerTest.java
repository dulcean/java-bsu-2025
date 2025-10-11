package lab1;

import lab1.model.line.ChemicalLine;
import lab1.model.line.ElectronicsLine;
import lab1.model.line.MechanicalLine;
import lab1.model.line.ProductionLine;
import lab1.model.product.ChemicalProduct;
import lab1.model.product.ElectronicProduct;
import lab1.model.product.MechanicalProduct;
import lab1.service.ProdAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProdAnalyzerTest {

    private ProdAnalyzer sampleAnalyzer() {
        var e1 = new ElectronicProduct("E1", "ElectronicProduct1", 20);
        var e2 = new ElectronicProduct("E2", "ElectronicProduct2", 40);
        var m1 = new MechanicalProduct("M1", "MechanicalProduct1", 42);
        var c1 = new ChemicalProduct("C1", "ChemicalProduct1", 67);

        var el = new ElectronicsLine("EL-1", 0.85);
        el.addProduct(e1);
        el.addProduct(e2);

        var ml = new MechanicalLine("ML-1", 0.67);
        ml.addProduct(m1);

        var cl = new ChemicalLine("CL-1", 0.42);
        cl.addProduct(c1);

        return new ProdAnalyzer(List.of(el, ml, cl));
    }

    @Test
    void getAllLines_returnsUnmodifiableCopy() {
        var analyzer = sampleAnalyzer();
        var lines = analyzer.getAllLines();
        assertEquals(3, lines.size());
        assertThrows(UnsupportedOperationException.class, () -> lines.add(null));
    }

    @Test
    void getHighEfficiencyLines_strictlyGreaterThanThreshold() {
        var analyzer = sampleAnalyzer();

        assertEquals(List.of("EL-1"), analyzer.getHighEfficiencyLines(0.8));
        assertEquals(List.of(), analyzer.getHighEfficiencyLines(0.85));
        assertEquals(List.of(), analyzer.getHighEfficiencyLines(0.9));
    }

    @Test
    void countProductsByCategory_correctlyAggregates() {
        var analyzer = sampleAnalyzer();
        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(3, counts.size());
        assertEquals(2L, counts.get("Electronics"));
        assertEquals(1L, counts.get("Mechanical"));
        assertEquals(1L, counts.get("Chemical"));
    }

    @Test
    void findMostLoadedLine_returnsTheLineWithMaxProducts() {
        var analyzer = sampleAnalyzer();
        var most = analyzer.findMostLoadedLine();
        assertTrue(most.isPresent());
        assertEquals("EL-1", most.get().getLineId());
    }

    @Test
    void findMostLoadedLine_emptyAnalyzer_returnsEmptyOptional() {
        var analyzer = new ProdAnalyzer(List.of());
        assertTrue(analyzer.findMostLoadedLine().isEmpty());
    }

    @Test
    void getAllProductsFromLines_returnsFlattenedUnmodifiableList() {
        var analyzer = sampleAnalyzer();
        var products = analyzer.getAllProductsFromLines();
        assertEquals(4, products.size());
        assertThrows(UnsupportedOperationException.class, () -> products.add(null));
    }

    @Test
    void calculateTotalProductionTime_sumsTimesAcrossAllLines() {
        var analyzer = sampleAnalyzer();
        assertEquals(169.0, analyzer.calculateTotalProductionTime());
    }

    @Test
    void constructor_makesDefensiveCopyOfLines() {
        var el = new ElectronicsLine("EL-X", 0.9);
        var lines = new ArrayList<ProductionLine<?>>();
        lines.add(el);
        var analyzer = new ProdAnalyzer(lines);

        lines.clear();
        assertEquals(1, analyzer.getAllLines().size());
    }
}
