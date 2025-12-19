package com.bank.game;

import java.util.ArrayList;
import java.util.List;

public class InvokerEngine {
    public enum Orb {
        QUAS, WEX, EXORT
    }

    private final List<Orb> currentOrbs = new ArrayList<>();

    public void addOrb(Orb orb) {
        currentOrbs.add(orb);
        if (currentOrbs.size() > 3)
            currentOrbs.remove(0);
    }

    public List<Orb> getCurrentOrbs() {
        return new ArrayList<>(currentOrbs);
    }

    public String invoke() {
        if (currentOrbs.size() < 3)
            return "NOT_ENOUGH";

        long q = currentOrbs.stream().filter(o -> o == Orb.QUAS).count();
        long w = currentOrbs.stream().filter(o -> o == Orb.WEX).count();
        long e = currentOrbs.stream().filter(o -> o == Orb.EXORT).count();

        if (q == 3)
            return "COLD_FREEZE";
        if (q == 2 && w == 1)
            return "GHOST_AUDIT";
        if (q == 2 && e == 1)
            return "SECURE_WALL";
        if (w == 3)
            return "EMP_TRANSFER";
        if (w == 2 && q == 1)
            return "FAST_WITHDRAW";
        if (w == 2 && e == 1)
            return "SPEED_DEPOSIT";
        if (e == 3)
            return "SUN_STRIKE";
        if (e == 2 && q == 1)
            return "FORGE_ACCOUNT";
        if (e == 2 && w == 1)
            return "CREDIT_METEOR";
        if (q == 1 && w == 1 && e == 1)
            return "DEAFENING_REPORT";

        return "UNKNOWN";
    }
}
