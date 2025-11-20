package com.banking.system.logic.actions;
import com.banking.system.database.AccountRepo;
import com.banking.system.model.Operation;

public class DepositAction implements IBankAction {
    @Override
    public void perform(Operation op, AccountRepo repo) throws Exception {
        var acc = repo.get(op.getToAcc()).orElseThrow(() -> new Exception("Счет не найден"));
        acc.secureAccess();
        try {
            if (acc.isBlocked()) throw new Exception("Счет заморожен");
            acc.setMoney(acc.getMoney() + op.getAmount());
            repo.updateBalanceAndStatus(acc);
        } finally {
            acc.releaseAccess();
        }
    }
}