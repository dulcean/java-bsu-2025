package strategy;

import model.Account;
import model.Transaction;

public class WithdrawStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx) throws Exception {
        Account acc = tx.getAccount();
        acc.getLock().lock();
        try {
            if (acc.isFrozen())
                throw new Exception("Account frozen");

            if (acc.getBalance().compareTo(tx.getAmount()) < 0)
                throw new Exception("Not enough funds");

            acc.setBalance(acc.getBalance().subtract(tx.getAmount()));
            tx.setStatus(Transaction.Status.SUCCESS);

        } finally {
            acc.getLock().unlock();
        }
    }
}
