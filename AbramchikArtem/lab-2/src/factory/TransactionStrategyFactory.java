package factory;

import model.Transaction;
import strategy.*;

public class TransactionStrategyFactory {

    public static TransactionStrategy getStrategy(Transaction.Action action) {
        return switch (action) {
            case DEPOSIT -> new DepositStrategy();
            case WITHDRAW -> new WithdrawStrategy();
            case FREEZE -> new FreezeStrategy();
            case TRANSFER -> new TransferStrategy();
        };
    }
}
