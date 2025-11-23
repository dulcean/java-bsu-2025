package bank.strategy;

import bank.model.Transaction;
import bank.model.enums.AccountStatus;
import bank.service.TransactionService;

public class FreezeStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx, TransactionService service) {
        var account = service.getAccount(tx.getAccountId());
        account.setStatus(AccountStatus.FROZEN);
        service.saveAccount(account);
    }
}

