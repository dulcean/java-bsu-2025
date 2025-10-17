package ui;

import factory.Factory;
import factory.productionLines.ProductionLine;
import factory.products.Product;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static ui.Colors.*;

public class CLInterface {
    private final Scanner in;
    private final PrintStream out;

    private Menu mainMenu;
    private Menu current;

    private final Factory factory;

    private double readDouble () {
        double d;
        while (true) {
            try {
                d = in.nextDouble();
                break;
            } catch (InputMismatchException e) {
                out.println("Double expected, try again:");
                in.nextLine();
            }
        }
        in.nextLine();
        return d;
    }

    private int readInt () {
        int i;
        while (true) {
            try {
                i = in.nextInt();
                break;
            } catch (InputMismatchException e) {
                out.println("Int expected, try again:");
                in.nextLine();
            }
        }
        in.nextLine();
        return i;
    }

    private void initMenu () {
        mainMenu = new Menu("Main menu");

        Menu Analyzer = new Menu("Analyzer", mainMenu);
        mainMenu.addEntry(Analyzer);
        Analyzer.addExec("Get High Efficiency Lines", () ->  {
            out.println("Threshold:");
            double threshold = readDouble();
            factory.getAnalyzer().getHighEfficiencyLines(threshold).forEach(l -> out.println(GREEN + l + RESET));
            return null;
        });
        Analyzer.addExec("Count Products By Category", () -> {
            factory.getAnalyzer().countProductsByCategory().forEach((String k, Long v) -> {out.println(k + " : " + v);});
            return null;
        });
        Analyzer.addExec("Get All Products From Lines", () -> {
            factory.getAnalyzer().getAllProductsFromLines().forEach(p -> {out.println(BLUE + p.getId() + RESET);});
            return null;
        });
        Analyzer.addExec("Find Most Loaded Line", () -> {
            Optional<ProductionLine <? extends Product>> line = factory.getAnalyzer().findMostLoadedLine();
            if (line.isPresent()) {
                out.println(GREEN + line.get().getId() + RESET);
            } else {
                out.println("No lines found");
            }
            return null;
        });
        Analyzer.addExec("Calculate Total Production Time", () -> {
            out.println(factory.getAnalyzer().CalculateTotalProductionTime());
            return null;
        });

        Menu ManageFactory = new Menu("Manage Factory", mainMenu);
        mainMenu.addEntry(ManageFactory);
        ManageFactory.addExec("Add Line", () -> {
            out.println("New Line id:");
            String id = in.nextLine();
            out.println("Efficiency:");
            double efficiency = readDouble();
            out.println("Type ([E]lectronic, [M]echanic, [C]hemical):");
            String type = in.nextLine();
            String res = switch (factory.AddLine(id, efficiency, type)) {
                case SUCCESS -> GREEN+"Success"+RESET;
                case ID_FAILURE -> RED+"Id already taken"+RESET;
                case TYPE_FAILURE -> RED+"Type not found. Available Types: Electronic, Chemical, Mechanic"+RESET;
                default -> RED+"UNKNOWN"+RESET;
            };
            out.println(res);
            return null;
        });
        ManageFactory.addExec("Modify Line", () -> {
            Menu lines = new Menu("Lines", ManageFactory);
            factory.getAnalyzer().getAllLines().forEach((line) -> {lines.addExec("Modify line " + GREEN + line.getId() + RESET, () -> {
                out.println("Modifying " + GREEN + line.getId() + RESET + " of type " + line.getCategory() + " and efficiency " + line.getEfficiency());
                Menu Modifications = new Menu("Modifications", lines);
                Modifications.addExec("Change Efficiency", () -> {
                    out.println("New efficiency: ");
                    double efficiency = readDouble();
                    line.setEfficiency(efficiency);
                    return null;
                });
                Modifications.addExec("Add Product", () -> {
                    out.println("Id: ");
                    String id = in.nextLine();
                    out.println("Name: ");
                    String name = in.nextLine();
                    out.println("Time: ");
                    int time = readInt();
                    out.println("Category: ");
                    String type = in.nextLine();
                    String res = switch (line.addProduct(id, name, time, type)) {
                        case ID_FAILURE ->  RED+"Id already taken"+RESET;
                        case TYPE_FAILURE -> RED+"Invalid category"+RESET;
                        case SUCCESS -> GREEN+"Success"+RESET;
                        default -> RED+"UNKNOWN"+RESET;
                    };
                    out.println(res);
                    return null;
                });
                Modifications.addExec("Remove Product", () -> {
                    Menu Products = new Menu("Products", Modifications);
                    for  (int i = 0; i < line.getProducts().size(); i++) {
                        int finalI = i;
                        Products.addExec("Remove product " + BLUE + line.getProducts().get(i).getId() + RESET, () -> {
                            line.removeProduct(finalI);
                            return null;
                        });
                    }
                    return Products;
                });
                Modifications.addExec("Remove Line", () -> {
                    factory.getAnalyzer().removeLine(line);
                    return null;
                });
                return Modifications;
            });});
            return lines;
        });
        current = mainMenu;
    }

    public CLInterface(Scanner in, PrintStream out, Factory factory) {
        this.in = in;
        this.out = out;
        this.factory = factory;
        initMenu();
    }

    public boolean step () {
        out.println(current.printed());
        String line = in.nextLine();
        ActionResult res = current.readResponce(line);
        switch (res) {
            case GO_NEXT: {
                MenuEntry exec = current.getNext();
                Menu next = (exec).exec();
                if (next == null) {current = mainMenu;}
                else {current = next;}
                break;
            }
            case UNKNOWN: {
                out.println(RED+"Unknow command. Try again."+RESET);
                break;
            }
            case FAILURE: {
                out.println(RED+"You cannot use this command here. Try again."+RESET);
                break;
            }
            case QUIT: {
                return false;
            }
        }
        return true;
    }
}
