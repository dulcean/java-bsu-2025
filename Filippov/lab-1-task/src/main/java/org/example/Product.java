package org.example;
public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTimeMinutes;

    protected Product(String id, String name, int productionTimeMinutes) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (productionTimeMinutes < 0) throw new IllegalArgumentException("productionTimeMinutes must be >= 0");

        this.id = id;
        this.name = name;
        this.productionTimeMinutes = productionTimeMinutes;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getProductionTimeMinutes() { return productionTimeMinutes; }

    public abstract String getCategory();

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " id=" + id +
                ", name=" + name +
                ", time=" + productionTimeMinutes + "min";
    }
}

class ElectronicProduct extends Product {
    public ElectronicProduct(String id, String name, int productionTimeMinutes) {
        super(id, name, productionTimeMinutes);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}

class MechanicalProduct extends Product {
    public MechanicalProduct(String id, String name, int productionTimeMinutes) {
        super(id, name, productionTimeMinutes);
    }

    @Override
    public String getCategory() {
        return "Mechanical";
    }
}

class ChemicalProduct extends Product {
    public ChemicalProduct(String id, String name, int productionTimeMinutes) {
        super(id, name, productionTimeMinutes);
    }

    @Override
    public String getCategory() {
        return "Chemical";
    }
}

class Main {
    public static void main(String[] args) {
        ElectronicsLine eLine1 = new ElectronicsLine("E-100", 0.85);
        ElectronicsLine eLine2 = new ElectronicsLine("E-200", 0.60);
        MechanicalLine mLine1 = new MechanicalLine("M-100", 0.92);
        ChemicalLine cLine1 = new ChemicalLine("C-50", 0.55);

        ElectronicProduct ep1 = new ElectronicProduct("EP-01", "SmartSensor", 30);
        ElectronicProduct ep2 = new ElectronicProduct("EP-02", "ControlBoard", 45);
        MechanicalProduct mp1 = new MechanicalProduct("MP-01", "GearBox", 120);
        ChemicalProduct cp1 = new ChemicalProduct("CP-01", "PolymerBatch", 240);

        eLine1.addProduct(ep1);
        eLine1.addProduct(ep2);

        mLine1.addProduct(mp1);
        cLine1.addProduct(cp1);

        try {
            Product naughty = mp1;
            if (!eLine2.canProduce(naughty)) {
                System.out.println("As expected: eLine2 cannot produce mp1");
            }
        } catch (Exception ex) {
            System.err.println("error: " + ex.getMessage());
        }

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.registerLine(eLine1);
        analyzer.registerLine(eLine2);
        analyzer.registerLine(mLine1);
        analyzer.registerLine(cLine1);

        eLine2.addProduct(new ElectronicProduct("EP-03", "BatteryPack", 20));
        eLine2.addProduct(new ElectronicProduct("EP-04", "Display", 25));

        mLine1.addProduct(new MechanicalProduct("MP-02", "Shaft", 60));
        mLine1.addProduct(new MechanicalProduct("MP-03", "Bearing", 10));

        System.out.println("All lines: " + analyzer.getAllLines());
        System.out.println("High efficiency lines (>0.8): " + analyzer.getHighEfficiencyLines(0.8));
        System.out.println("Products count by category: " + analyzer.countProductsByCategory());

        analyzer.findMostLoadedLine().ifPresent(line ->
                System.out.println("Most loaded line: " + line.getLineId() + " with " + line.getProducts().size() + " products"));

        System.out.println("All products combined: " + analyzer.getAllProductsFromLines());
        System.out.println("Total production time (minutes): " + analyzer.calculateTotalProductionTime());

        assert analyzer.getAllProductsFromLines().size() == analyzer.countProductsByCategory().values().stream().mapToLong(Long::longValue).sum();
    }
}
