package tests;

import analyzer.*;
import line.*;
import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestIt {

    private ElectronicsLine electronicsLine;
    private MechanicalLine mechanicalLine;
    private ChemicalLine chemicalLine;
    private ProdAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        electronicsLine = new ElectronicsLine("EL1", 0.9);
        mechanicalLine = new MechanicalLine("ME1", 0.6);
        chemicalLine = new ChemicalLine("CH1", 0.4);

        electronicsLine.addProduct(new ElectronicProduct("E1", "Smartphone", 5));
        electronicsLine.addProduct(new ElectronicProduct("E2", "Tablet", 7));

        mechanicalLine.addProduct(new MechanicalProduct("M1", "Engine", 10));

        chemicalLine.addProduct(new ChemicalProduct("C1", "Paint", 3));
        chemicalLine.addProduct(new ChemicalProduct("C2", "Glue", 2));
        chemicalLine.addProduct(new ChemicalProduct("C3", "Resin", 6));

        analyzer = new ProdAnalyzer(Arrays.asList(electronicsLine, mechanicalLine, chemicalLine));
    }

    @Test
    void testInitializationOfProductionLine() {
        assertEquals("EL1", electronicsLine.getLineId());
        assertEquals(0.9, electronicsLine.getEfficiency());
        assertEquals(2, electronicsLine.getProducts().size());
        assertTrue(electronicsLine.canProduce(new ElectronicProduct("E3", "Phone", 4)));
        assertFalse(electronicsLine.canProduce(new MechanicalProduct("M2", "Pump", 5)));
    }

    @Test
    void testValidationRejectsIncompatibleProduct() {
        assertThrows(IllegalArgumentException.class, () ->
                electronicsLine.addProduct(new MechanicalProduct("M2", "Compressor", 5))
        );
    }

    @Test
    void testAllMethodsOfProdAnalyzer() {
        List<ProductionLine<? extends Product>> allLines = analyzer.getAllLines();
        assertEquals(3, allLines.size());
        assertTrue(allLines.contains(electronicsLine));
        assertTrue(allLines.contains(mechanicalLine));
        assertTrue(allLines.contains(chemicalLine));

        List<String> efficient = analyzer.getHighEfficiencyLines(0.5);
        assertEquals(Arrays.asList("EL1", "ME1"), efficient);

        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(2, counts.get("Electronics"));
        assertEquals(1, counts.get("Mechanical"));
        assertEquals(3, counts.get("Chemical"));
        assertEquals(3, counts.size());

        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();
        assertTrue(mostLoaded.isPresent());
        assertEquals("CH1", mostLoaded.get().getLineId());
        assertEquals(3, mostLoaded.get().getProducts().size());

        List<Product> allProducts = analyzer.getAllProductsFromLines();
        assertEquals(6, allProducts.size());
        assertTrue(allProducts.stream().anyMatch(p -> p.getName().equals("Tablet")));
        assertTrue(allProducts.stream().anyMatch(p -> p instanceof ChemicalProduct));

        double totalTime = analyzer.calculateTotalProductionTime();
        assertEquals(33, totalTime);
    }
}
