package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLineTest {
    private ElectronicsLine eLine;
    private MechanicalLine mLine;
    private ChemicalLine cLine;

    @BeforeEach
    void setup() {
        eLine = new ElectronicsLine("E-1", 0.5);
        mLine = new MechanicalLine("M-1", 0.75);
        cLine = new ChemicalLine("C-1", 0.9);
    }

    @Test
    void canProduceChecksType() {
        ElectronicProduct ep = new ElectronicProduct("EP-1", "Board", 10);
        MechanicalProduct mp = new MechanicalProduct("MP-1", "Bolt", 5);
        ChemicalProduct cp = new ChemicalProduct("CP-1", "Solvent", 60);
        assertTrue(eLine.canProduce(ep));
        assertFalse(eLine.canProduce(mp));
        assertFalse(eLine.canProduce(cp));
        assertTrue(mLine.canProduce(mp));
        assertFalse(mLine.canProduce(ep));
        assertFalse(mLine.canProduce(cp));
        assertTrue(cLine.canProduce(cp));
        assertFalse(cLine.canProduce(ep));
        assertFalse(cLine.canProduce(mp));
    }

    @Test
    void addProductAcceptsCompatibleAndRejectsIncompatible() {
        ElectronicProduct ep = new ElectronicProduct("EP-2", "Sensor", 20);
        eLine.addProduct(ep);
        assertEquals(1, eLine.getProducts().size());

        MechanicalProduct mp = new MechanicalProduct("MP-2", "Shaft", 40);

        ProductionLine raw = eLine;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> raw.addProduct(mp));
        assertTrue(
                ex.getMessage().toLowerCase().contains("not compatible") ||
                        ex.getMessage().toLowerCase().contains("incompatible") ||
                        ex.getMessage().toLowerCase().contains("cannot produce")
        );

        ChemicalProduct cp = new ChemicalProduct("CP-2", "Batch", 120);
        cLine.addProduct(cp);
        assertEquals(1, cLine.getProducts().size());

        ProductionLine rawC = cLine;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> rawC.addProduct(ep));
        assertTrue(
                ex2.getMessage().toLowerCase().contains("not compatible") ||
                        ex2.getMessage().toLowerCase().contains("incompatible") ||
                        ex2.getMessage().toLowerCase().contains("cannot produce")
        );
    }

    @Test
    void efficiencyBoundsValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ElectronicsLine("E-bad1", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new ElectronicsLine("E-bad2", 1.1));

        ElectronicsLine e0 = new ElectronicsLine("E-0", 0.0);
        ElectronicsLine e1 = new ElectronicsLine("E-1", 1.0);
        assertEquals(0.0, e0.getEfficiency());
        assertEquals(1.0, e1.getEfficiency());
    }

    @Test
    void productsListIsUnmodifiableFromGetter() {
        ElectronicProduct ep = new ElectronicProduct("EP-3", "Display", 25);
        eLine.addProduct(ep);
        List<ElectronicProduct> list = eLine.getProducts();
        assertThrows(UnsupportedOperationException.class, () -> list.add(new ElectronicProduct("EP-x", "X", 1)));
    }

    @Test
    void toStringContainsImportantInfo() {
        ElectronicProduct ep = new ElectronicProduct("EP-4", "X", 5);
        eLine.addProduct(ep);
        String s = eLine.toString();
        assertTrue(s.contains("E-1"));
        assertTrue(s.contains("eff="));
        assertTrue(s.contains("products=1"));
    }
}