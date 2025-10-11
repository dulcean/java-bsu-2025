package lab1;

import lab1.model.product.ChemicalProduct;
import lab1.model.product.ElectronicProduct;
import lab1.model.product.MechanicalProduct;
import lab1.model.product.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductValidationTest {

    @Test
    void electronicProduct_validFields_areExposed() {
        ElectronicProduct p = new ElectronicProduct("E1", "Sensor", 15);
        assertEquals("E1", p.getId());
        assertEquals("Sensor", p.getName());
        assertEquals(15, p.getProductionTime());
        assertEquals("Electronics", p.getCategory());
        String s = p.toString();
        assertTrue(s.contains("E1"));
        assertTrue(s.contains("Sensor"));
        assertTrue(s.contains("Electronics"));
    }

    @Test
    void mechanicalProduct_validCategory() {
        Product p = new MechanicalProduct("M1", "Gear", 5);
        assertEquals("Mechanical", p.getCategory());
    }

    @Test
    void chemicalProduct_validCategory() {
        Product p = new ChemicalProduct("C1", "Acid", 7);
        assertEquals("Chemical", p.getCategory());
    }

    @Test
    void product_constructor_idMustNotBeBlank() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct(null, "Name", 1));
        assertTrue(ex1.getMessage().toLowerCase().contains("id"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("   ", "Name", 1));
        assertTrue(ex2.getMessage().toLowerCase().contains("id"));
    }

    @Test
    void product_constructor_nameMustNotBeBlank() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("E1", null, 1));
        assertTrue(ex1.getMessage().toLowerCase().contains("name"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("E1", "   ", 1));
        assertTrue(ex2.getMessage().toLowerCase().contains("name"));
    }

    @Test
    void product_constructor_productionTimeMustBePositive() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("E1", "Name", 0));
        assertTrue(ex1.getMessage().toLowerCase().contains("productiontime"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("E1", "Name", -3));
        assertTrue(ex2.getMessage().toLowerCase().contains("productiontime"));
    }
}
