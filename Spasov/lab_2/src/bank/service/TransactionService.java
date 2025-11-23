package bank.service;

import bank.db.AccountRepository;
import bank.db.TransactionRepository;
import bank.db.jdbc.JdbcAccountRepository;
import bank.db.jdbc.JdbcTransactionRepository;
import bank.factory.TransactionStrategyFactory;
import bank.model.Account;
import bank.model.Transaction;
import bank.model.enums.TransactionStatus;
import bank.strategy.TransactionStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    private final TransactionRepository transactionRepository = new JdbcTransactionRepository();
    private final AccountRepository accountRepository = new JdbcAccountRepository();

    /**
     * Единая точка выполнения транзакции
     */
    public void execute(Transaction tx) {
        validateTransaction(tx);

        TransactionStrategy strategy = TransactionStrategyFactory.forType(tx.getType());

        tx.setStatus(TransactionStatus.PENDING);

        try {
            strategy.execute(tx, this);
            tx.setStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            tx.setStatus(TransactionStatus.FAILED);
            throw e;
        } finally {
            tx.setTimestamp(LocalDateTime.now());
            transactionRepository.save(tx);
        }
    }

    public Account getAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found!"));
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null ||
                transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive!");
        }
        if (transaction.getAccountId() == null) {
            throw new IllegalArgumentException("Transaction must have an accountId!");
        }
    }
}
