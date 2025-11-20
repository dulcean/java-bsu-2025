package com.banking.system.logic.actions;
import com.banking.system.database.AccountRepo;
import com.banking.system.model.BankAccount;
import com.banking.system.model.Operation;

public class TransferAction implements IBankAction {
    @Override
    public void perform(Operation op, AccountRepo repo) throws Exception {
        var from = repo.get(op.getFromAcc()).orElseThrow(() -> new Exception("Отправитель не найден"));
        var to = repo.get(op.getToAcc()).orElseThrow(() -> new Exception("Получатель не найден"));

        BankAccount first = from.getUuid().compareTo(to.getUuid()) < 0 ? from : to;
        BankAccount second = from.getUuid().compareTo(to.getUuid()) < 0 ? to : from;

        first.secureAccess();
        second.secureAccess();
        try {
            if (from.isBlocked() || to.isBlocked()) throw new Exception("Один из счетов заморожен");
            if (from.getMoney() < op.getAmount()) throw new Exception("Недостаточно средств");

            from.setMoney(from.getMoney() - op.getAmount());
            to.setMoney(to.getMoney() + op.getAmount());

            repo.updateBalanceAndStatus(from);
            repo.updateBalanceAndStatus(to);
        } finally {
            second.releaseAccess();
            first.releaseAccess();
        }
    }
}