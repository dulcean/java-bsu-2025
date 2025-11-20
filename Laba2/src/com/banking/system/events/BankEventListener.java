package com.banking.system.events;
import com.banking.system.model.Operation;

public interface BankEventListener {
    void onOperationFinished(Operation op);
}
