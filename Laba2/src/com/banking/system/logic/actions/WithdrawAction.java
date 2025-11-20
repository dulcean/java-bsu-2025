package com.banking.system.logic.actions;
import com.banking.system.database.AccountRepo;
import com.banking.system.model.Operation;

public class WithdrawAction implements IBankAction {
    @Override
    public void perform(Operation op, AccountRepo repo) throws Exception {
        var acc = repo.get(op.getFromAcc()).orElseThrow(() -> new Exception("Счет списания не найден"));
        acc.secureAccess();
        try {
            if (acc.isBlocked()) throw new Exception("Счет заморожен");
            if (acc.getMoney() < op.getAmount()) throw new Exception("Мало денег");
            acc.setMoney(acc.getMoney() - op.getAmount());
            repo.updateBalanceAndStatus(acc);
        } finally {
            acc.releaseAccess();
        }
    }
}