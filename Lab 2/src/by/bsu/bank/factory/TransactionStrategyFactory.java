package by.bsu.bank.factory;

import by.bsu.bank.model.transaction.TransactionType;
import by.bsu.bank.repository.AccountRepository;
import by.bsu.bank.strategy.DepositTransactionStrategy;
import by.bsu.bank.strategy.FreezeTransactionStrategy;
import by.bsu.bank.strategy.TransactionStrategy;
import by.bsu.bank.strategy.TransferTransactionStrategy;
import by.bsu.bank.strategy.WithdrawTransactionStrategy;

import java.util.EnumMap;
import java.util.Map;

public class TransactionStrategyFactory {
    private final Map<TransactionType, TransactionStrategy> strategies = new EnumMap<>(TransactionType.class);

    public TransactionStrategyFactory(AccountRepository accountRepository) {
        strategies.put(TransactionType.DEPOSIT, new DepositTransactionStrategy(accountRepository));
        strategies.put(TransactionType.WITHDRAW, new WithdrawTransactionStrategy(accountRepository));
        strategies.put(TransactionType.FREEZE, new FreezeTransactionStrategy(accountRepository));
        strategies.put(TransactionType.TRANSFER, new TransferTransactionStrategy(accountRepository));
    }

    public TransactionStrategy createStrategy(TransactionType transactionType) {
        TransactionStrategy strategy = strategies.get(transactionType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy registered for transaction type " + transactionType);
        }
        return strategy;
    }
}
