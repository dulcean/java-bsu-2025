package com.bank.patterns;

import com.bank.model.Account;
import com.bank.model.ActionType;
import com.bank.model.Transaction;
import java.util.Map;
import java.util.UUID;

public class StrategyFactory {
    public static TransactionStrategy getStrategy(ActionType type) {
        switch (type) {
            case DEPOSIT:
                return new DepositStrategy();
            case WITHDRAW:
                return new WithdrawStrategy();
            case FREEZE:
                return new FreezeStrategy();
            case TRANSFER:
                return new TransferStrategy();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    private static class DepositStrategy implements TransactionStrategy {
        @Override
        public void execute(Account account, Transaction tx, Map<UUID, Account> repo) {
            if (account.isFrozen())
                throw new IllegalStateException("Account is FROZEN. Cannot deposit.");
            account.setBalance(account.getBalance().add(tx.getAmount()));
        }
    }

    private static class WithdrawStrategy implements TransactionStrategy {
        @Override
        public void execute(Account account, Transaction tx, Map<UUID, Account> repo) {
            if (account.isFrozen())
                throw new IllegalStateException("Account is FROZEN. Cannot withdraw.");
            if (account.getBalance().compareTo(tx.getAmount()) < 0)
                throw new IllegalStateException("Insufficient funds");
            account.setBalance(account.getBalance().subtract(tx.getAmount()));
        }
    }

    private static class FreezeStrategy implements TransactionStrategy {
        @Override
        public void execute(Account account, Transaction tx, Map<UUID, Account> repo) {
            boolean newState = !account.isFrozen();
            account.setFrozen(newState);
        }
    }

    private static class TransferStrategy implements TransactionStrategy {
        @Override
        public void execute(Account fromAccount, Transaction tx, Map<UUID, Account> repo) {
            if (fromAccount.isFrozen())
                throw new IllegalStateException("Source Account is frozen");
            Account toAccount = repo.get(tx.getTargetAccountId());
            if (toAccount == null)
                throw new IllegalArgumentException("Target account not found");
            if (toAccount.isFrozen())
                throw new IllegalStateException("Target Account is frozen");
            if (fromAccount.getBalance().compareTo(tx.getAmount()) < 0)
                throw new IllegalStateException("Insufficient funds");

            fromAccount.setBalance(fromAccount.getBalance().subtract(tx.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(tx.getAmount()));
        }
    }
}
