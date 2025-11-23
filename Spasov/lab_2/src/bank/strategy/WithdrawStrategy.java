package bank.strategy;

import bank.model.Transaction;
import bank.model.enums.AccountStatus;
import bank.service.TransactionService;

public class WithdrawStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx, TransactionService service) {
        var account = service.getAccount(tx.getAccountId());

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is frozen!");
        }

        if (account.getBalance().compareTo(tx.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds!");
        }

        account.setBalance(account.getBalance().subtract(tx.getAmount()));
        service.saveAccount(account);
    }
}

