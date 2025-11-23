package strategy;

import model.Transaction;

public class FreezeStrategy implements TransactionStrategy {
    @Override
    public void execute(Transaction tx) {
        tx.getAccount().freeze();
        tx.setStatus(Transaction.Status.SUCCESS);
    }
}
