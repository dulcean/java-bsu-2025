package ui;

import java.util.ArrayList;
import java.util.List;

import static ui.Colors.RED;
import static ui.Colors.RESET;

public class Menu implements MenuEntry {
    private List<MenuEntry> entries;
    private Menu parent;
    private MenuEntry next;
    private final String name;
    private int page = 0;

    public Menu (String name) {
        this(name, null);
    }

    public Menu (String name, Menu parent) {
        this.name = name;
        this.entries = new ArrayList<>();
        this.parent = parent;
    }

    public void addEntry (MenuEntry entry) {
        entries.add(entry);
    }

    public void addExec (String name, EntryAction action) {
        entries.add(new MenuEntry() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Menu exec() {
                return action.run();
            }
        });
    }

    public void addExec (MenuEntry entry) {
        entries.add(entry);
    }

    public void removeEntry (int id) {
        entries.remove(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Menu exec() {
        return this;
    }

    public MenuEntry getNext() {
        return this.next;
    }

    public String printed() {
        StringBuilder sb = new StringBuilder();
        sb.append(RED + "q" + RESET + " : quit\n");
        if (parent != null) {
            sb.append(RED + "b" + RESET + " : go back\n");
        }
        for (int i = page * 10; i < (page * 10) + 10 && i < entries.size(); i++) {
            sb.append(RED).append((i+1) % 10).append(RESET).append(" : ").append(entries.get(i).getName()).append("\n");
        }
        if (page > 0) {
            sb.append(RED + "[" + RESET + " : previous page\n");
        }
        if (page * 10 + 10 < entries.size()) {
            sb.append(RED + "]" + RESET + " : next page\n");
        }
        return sb.toString();
    }

    public ActionResult readResponce (String responce) {
        if (responce.length() != 1) {return ActionResult.UNKNOWN;}
        char c = responce.charAt(0);
        if (c == 'q' || c == 'Q') {
            return ActionResult.QUIT;
        }
        if (c == 'b' || c == 'B') {
            if (parent == null) {
                return ActionResult.FAILURE;
            }
            next = parent;
            return ActionResult.GO_NEXT;
        }
        if ('0' <= c && c <= '9') {
            if (page * 10 + (c - '0' + 9) % 10  >= entries.size()) {
                return ActionResult.FAILURE;
            }
            next = entries.get(page * 10 + (c - '0' + 9) % 10);
            return ActionResult.GO_NEXT;
        }
        if (c == '[') {
            if (page <= 0) return ActionResult.FAILURE;
            --page;
            return ActionResult.SWITCH_PAGE;
        }
        if (c == ']') {
            if (page * 10 + 10 >= entries.size()) {
                return ActionResult.FAILURE;
            }
            ++page;
            return ActionResult.SWITCH_PAGE;
        }
        return ActionResult.UNKNOWN;
    }
}
