package bank.strategy;

import bank.model.Transaction;
import bank.model.enums.AccountStatus;
import bank.service.TransactionService;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx, TransactionService service) {
        var account = service.getAccount(tx.getAccountId());

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is frozen!");
        }

        account.setBalance(account.getBalance().add(tx.getAmount()));
        service.saveAccount(account);
    }
}

