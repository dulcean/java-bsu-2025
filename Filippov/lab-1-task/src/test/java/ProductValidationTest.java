package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    @Test
    void electronicProductCategoryAndToString() {
        ElectronicProduct p = new ElectronicProduct("EP-1", "Sensor", 15);
        assertEquals("Electronics", p.getCategory());
        String s = p.toString();
        assertTrue(s.contains("EP-1"));
        assertTrue(s.contains("Sensor"));
        assertTrue(s.contains("15min"));
    }

    @Test
    void mechanicalAndChemicalCategories() {
        MechanicalProduct m = new MechanicalProduct("MP-1", "Gear", 120);
        ChemicalProduct c = new ChemicalProduct("CP-1", "Solvent", 300);
        assertEquals("Mechanical", m.getCategory());
        assertEquals("Chemical", c.getCategory());
    }

    @Test
    void productConstructorValidatesArguments() {
        assertThrows(IllegalArgumentException.class, () -> new ElectronicProduct(null, "X", 10));
        assertThrows(IllegalArgumentException.class, () -> new ElectronicProduct("id", "", 10));
        assertThrows(IllegalArgumentException.class, () -> new ElectronicProduct("id", "Name", -1));
    }
}
