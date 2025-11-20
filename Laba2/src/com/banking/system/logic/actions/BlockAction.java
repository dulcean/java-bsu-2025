package com.banking.system.logic.actions;
import com.banking.system.database.AccountRepo;
import com.banking.system.model.Operation;

public class BlockAction implements IBankAction {
    @Override
    public void perform(Operation op, AccountRepo repo) throws Exception {
        var acc = repo.get(op.getFromAcc()).orElseThrow(() -> new Exception("Счет не найден"));
        acc.secureAccess();
        try {
            acc.setBlocked(true);
            repo.updateBalanceAndStatus(acc);
        } finally {
            acc.releaseAccess();
        }
    }
}