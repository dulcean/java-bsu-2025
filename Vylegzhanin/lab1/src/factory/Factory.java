package factory;

import factory.productionLines.ChemicalsLine;
import factory.productionLines.ElectronicsLine;
import factory.productionLines.MechanicalsLine;
import factory.productionLines.ProductionLine;
import factory.products.Product;
import ui.CLInterface;

import java.util.Scanner;

public class Factory {
    private final ProdAnalyzer analyzer;
    private final CLInterface clInterface;

    public Factory () {
        analyzer = new ProdAnalyzer();
        clInterface = new CLInterface(new Scanner(System.in), System.out, this);
    }

    public ProdAnalyzer getAnalyzer() {
        return analyzer;
    }

    public FactoryOperationResult AddLine (String id, double efficiency, String type) {
        if (getAnalyzer().getAllLines().stream().map(ProductionLine::getId).anyMatch((String x) -> {return x.contentEquals(id);})) {
            return FactoryOperationResult.ID_FAILURE;
        }
        switch (type.toLowerCase()) {
            case "chemical": case "c":{
                analyzer.insertLine(new ChemicalsLine(id, efficiency));
                break;
            }
            case "electronic": case "e":{
                analyzer.insertLine(new ElectronicsLine(id, efficiency));
                break;
            }
            case "mechanic": case "m":{
                analyzer.insertLine(new MechanicalsLine(id, efficiency));
                break;
            }
            default: {
                return FactoryOperationResult.TYPE_FAILURE;
            }
        }
        return FactoryOperationResult.SUCCESS;
    }

    public void removeLine (ProductionLine<? extends Product> line) {
        getAnalyzer().removeLine(line);
    }

    public void runCLI () {
        while (clInterface.step());
    }
}
