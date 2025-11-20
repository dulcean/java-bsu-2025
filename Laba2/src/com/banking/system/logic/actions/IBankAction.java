package com.banking.system.logic.actions;

import com.banking.system.database.AccountRepo;
import com.banking.system.model.Operation;

public interface IBankAction {
    void perform(Operation op, AccountRepo repo) throws Exception;
}