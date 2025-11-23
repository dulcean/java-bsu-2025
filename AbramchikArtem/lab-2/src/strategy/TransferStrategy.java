package strategy;

import model.Account;
import model.Transaction;

public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx) throws Exception {
        Account from = tx.getAccount();
        Account to = tx.getTargetAccount();

        Account first = from.getId().compareTo(to.getId()) < 0 ? from : to;
        Account second = from == first ? to : from;

        first.getLock().lock();
        second.getLock().lock();
        try {
            if (from.isFrozen())
                throw new Exception("Frozen account");

            if (from.getBalance().compareTo(tx.getAmount()) < 0)
                throw new Exception("Insufficient funds");

            from.setBalance(from.getBalance().subtract(tx.getAmount()));
            to.setBalance(to.getBalance().add(tx.getAmount()));

            tx.setStatus(Transaction.Status.SUCCESS);

        } finally {
            second.getLock().unlock();
            first.getLock().unlock();
        }
    }
}
