package bank.observer;

import bank.model.Transaction;

public class ConsoleNotifier implements TransactionObserver {

    @Override
    public void update(Transaction transaction) {
        System.out.printf(
                "Transaction %s updated:\n" +
                        "  type:   %s\n" +
                        "  status: %s\n" +
                        "  from account: %s\n",
                transaction.getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAccountId()
        );

        if (transaction.getTargetAccountId() != null) {
            System.out.printf("  to   account: %s\n", transaction.getTargetAccountId());
        }

        if (transaction.getAccountBalanceAfter() != null) {
            System.out.printf("  balance(from): %s\n", transaction.getAccountBalanceAfter());
        }

        if (transaction.getTargetAccountBalanceAfter() != null) {
            System.out.printf("  balance(to):   %s\n", transaction.getTargetAccountBalanceAfter());
        }

        System.out.println("---------------------------------\n");
    }
}
