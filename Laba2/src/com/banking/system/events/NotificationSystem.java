package com.banking.system.events;

import com.banking.system.model.Operation;
import java.util.ArrayList;
import java.util.List;

public class NotificationSystem {
    private final List<BankEventListener> listeners = new ArrayList<>();

    public synchronized void addListener(BankEventListener l) { listeners.add(l); }

    public void notifyAll(Operation op) {
        for (var l : listeners) l.onOperationFinished(op);
    }
}