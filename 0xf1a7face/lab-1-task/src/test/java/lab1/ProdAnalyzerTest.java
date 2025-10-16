package lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ProdAnalyzerTest {

    private ProdAnalyzer analyzer;
    private ElectronicsLine electronicsLine;
    private MechanicalLine mechanicalLine;
    private ChemicalLine chemicalLine;

    @BeforeEach
    void setUp() {
        var ep1 = new ElectronicProduct("E1", "Smartphone", 30);
        var ep2 = new ElectronicProduct("E2", "Laptop", 120);

        var mp1 = new MechanicalProduct("M1", "Gear", 45);
        var mp2 = new MechanicalProduct("M2", "Bearing", 20);
        var mp3 = new MechanicalProduct("M3", "Shaft", 60);

        var cp1 = new ChemicalProduct("C1", "Solvent", 10);

        electronicsLine = new ElectronicsLine("EL-01", Arrays.asList(ep1, ep2),
                0.75);
        mechanicalLine = new MechanicalLine("ML-02", Arrays.asList(mp1, mp2, mp3),
                0.5);
        chemicalLine = new ChemicalLine("CL-03", Arrays.asList(cp1), 1);

        analyzer = new ProdAnalyzer(Arrays.asList(electronicsLine, mechanicalLine,
                chemicalLine));
    }

    @Test
    void getAllLines_returnsAllLines() {
        var lines = analyzer.getAllLines();
        assertThat(lines).hasSize(3);
        assertThat(lines).containsExactlyInAnyOrder(electronicsLine, mechanicalLine,
                chemicalLine);
    }

    @Test
    void getHighEfficiencyLines_returnsCorrectLineIds() {
        var highEffLines = analyzer.getHighEfficiencyLines(0.6);
        assertThat(highEffLines).containsExactlyInAnyOrder("EL-01", "CL-03");
        assertThat(highEffLines).doesNotContain("ML-02");
    }

    @Test
    void countProductsByCategory_returnsCorrectCounts() {
        var counts = analyzer.countProductsByCategory();
        assertThat(counts).hasSize(3);
        assertThat(counts).containsEntry("Electronics", 2L);
        assertThat(counts).containsEntry("Mechanical", 3L);
        assertThat(counts).containsEntry("Chemical", 1L);
    }

    @Test
    void findMostLoadedLine_returnsMechanicalLine() {
        var mostLoaded = analyzer.findMostLoadedLine();
        assertThat(mostLoaded).isPresent();
        assertThat(mostLoaded.get()).isEqualTo(mechanicalLine);
    }

    @Test
    void findMostLoadedLine_returnsEmptyWhenNoLines() {
        ProdAnalyzer emptyAnalyzer = new ProdAnalyzer(Collections.emptyList());
        var result = emptyAnalyzer.findMostLoadedLine();
        assertThat(result).isEmpty();
    }

    @Test
    void getAllProductsFromLines_returnsAllProducts() {
        var allProducts = analyzer.getAllProductsFromLines();
        assertThat(allProducts).hasSize(6);
        var productIds = allProducts.stream().map(Product::getId).collect(Collectors.toSet());
        assertThat(productIds).containsExactlyInAnyOrder("E1", "E2", "M1", "M2",
                "M3", "C1");
    }

    @Test
    void getAllProductsFromLines_returnsEmptyListWhenNoLines() {
        var emptyAnalyzer = new ProdAnalyzer(Collections.emptyList());
        var products = emptyAnalyzer.getAllProductsFromLines();
        assertThat(products).isEmpty();
    }

    @Test
    void calculateTotalProductionTime_returnsCorrectTime() {
        assertThat(analyzer.calculateTotalProductionTime()).isEqualTo(185.0);
    }
}
