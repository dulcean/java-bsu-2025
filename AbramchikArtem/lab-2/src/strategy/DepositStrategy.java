package strategy;

import model.Account;
import model.Transaction;
import java.math.BigDecimal;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx) {
        Account acc = tx.getAccount();
        acc.getLock().lock();
        try {
            acc.setBalance(acc.getBalance().add(tx.getAmount()));
            tx.setStatus(Transaction.Status.SUCCESS);
        } finally {
            acc.getLock().unlock();
        }
    }
}
