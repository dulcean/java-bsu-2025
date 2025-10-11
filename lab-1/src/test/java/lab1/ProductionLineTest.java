package lab1;

import lab1.model.line.ChemicalLine;
import lab1.model.line.ElectronicsLine;
import lab1.model.line.MechanicalLine;
import lab1.model.line.ProductionLine;
import lab1.model.product.ChemicalProduct;
import lab1.model.product.ElectronicProduct;
import lab1.model.product.MechanicalProduct;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionLineTest {

    @Test
    void constructor_validatesLineIdAndEfficiency() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> new MechanicalLine(null, 0.5));
        assertTrue(e1.getMessage().toLowerCase().contains("lineid"));

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> new MechanicalLine("   ", 0.5));
        assertTrue(e2.getMessage().toLowerCase().contains("lineid"));

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class,
                () -> new MechanicalLine("ML-1", -0.01));
        assertTrue(e3.getMessage().toLowerCase().contains("efficiency"));

        IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class,
                () -> new MechanicalLine("ML-1", 1.01));
        assertTrue(e4.getMessage().toLowerCase().contains("efficiency"));
    }

    @Test
    void getters_returnConfiguredValues() {
        MechanicalLine ml = new MechanicalLine("ML-1", 0.67);
        assertEquals("ML-1", ml.getLineId());
        assertEquals(0.67, ml.getEfficiency());
        assertEquals(0, ml.getProducts().size());
    }

    @Test
    void addProduct_null_throws() {
        ElectronicsLine el = new ElectronicsLine("EL-1", 0.85);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> el.addProduct(null));
        assertTrue(ex.getMessage().toLowerCase().contains("product"));
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void addProduct_wrongType_throws_usingErasure() {
        ProductionLine rawEl = new ElectronicsLine("EL-1", 0.85);
        MechanicalProduct mp = new MechanicalProduct("M1", "Gear", 5);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rawEl.addProduct(mp));
        assertTrue(ex.getMessage().contains("EL-1"));
    }

    @Test
    void addProduct_correctType_addsAndListIsUnmodifiable() {
        ElectronicsLine el = new ElectronicsLine("EL-1", 0.85);
        ElectronicProduct e1 = new ElectronicProduct("E1", "Sensor", 10);
        el.addProduct(e1);
        List<ElectronicProduct> products = el.getProducts();
        assertEquals(1, products.size());
        assertEquals("E1", products.get(0).getId());
        assertThrows(UnsupportedOperationException.class, () -> products.add(e1));
    }

    @Test
    void canProduce_matchesRespectiveTypes() {
        var el = new ElectronicsLine("EL-1", 0.9);
        var ml = new MechanicalLine("ML-1", 0.6);
        var cl = new ChemicalLine("CL-1", 0.4);

        assertTrue(el.canProduce(new ElectronicProduct("E1", "Chip", 5)));
        assertFalse(el.canProduce(new MechanicalProduct("M1", "Gear", 5)));
        assertFalse(el.canProduce(new ChemicalProduct("C1", "Acid", 5)));

        assertTrue(ml.canProduce(new MechanicalProduct("M2", "Bolt", 5)));
        assertFalse(ml.canProduce(new ElectronicProduct("E2", "Board", 5)));

        assertTrue(cl.canProduce(new ChemicalProduct("C2", "Base", 5)));
        assertFalse(cl.canProduce(new ElectronicProduct("E3", "Board", 5)));
    }
}
