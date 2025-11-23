package bank.strategy;

import bank.model.Transaction;
import bank.model.enums.AccountStatus;
import bank.service.TransactionService;

public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx, TransactionService service) {
        if (tx.getTargetAccountId() == null) {
            throw new IllegalArgumentException("Target account required for transfer!");
        }

        var source = service.getAccount(tx.getAccountId());
        var target = service.getAccount(tx.getTargetAccountId());

        if (source.getStatus() == AccountStatus.FROZEN ||
                target.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("One of the accounts is frozen!");
        }

        if (source.getBalance().compareTo(tx.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds!");
        }

        source.setBalance(source.getBalance().subtract(tx.getAmount()));
        target.setBalance(target.getBalance().add(tx.getAmount()));

        service.saveAccount(source);
        service.saveAccount(target);
    }
}

