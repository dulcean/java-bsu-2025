package bank.factory;

import bank.model.enums.TransactionType;
import bank.strategy.*;

public class TransactionStrategyFactory {
    public static TransactionStrategy forType(TransactionType t) {
        return switch (t) {
            case DEPOSIT -> new DepositStrategy();
            case WITHDRAW -> new WithdrawStrategy();
            case FREEZE -> new FreezeStrategy();
            case TRANSFER -> new TransferStrategy();
        };
    }
}
