package by.bsu.bank.service;

import by.bsu.bank.async.AsyncTransactionProcessor;
import by.bsu.bank.command.DefaultTransactionCommand;
import by.bsu.bank.command.TransactionCommand;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.DepositTransaction;
import by.bsu.bank.model.transaction.FreezeTransaction;
import by.bsu.bank.model.transaction.TransactionType;
import by.bsu.bank.model.transaction.TransferTransaction;
import by.bsu.bank.model.transaction.WithdrawTransaction;
import by.bsu.bank.service.dto.TransactionRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Future;

public class TransactionService {
    private final AsyncTransactionProcessor asyncTransactionProcessor;
    private final TransactionExecutionService transactionExecutionService;

    public TransactionService(AsyncTransactionProcessor asyncTransactionProcessor,
                              TransactionExecutionService transactionExecutionService) {
        this.asyncTransactionProcessor = asyncTransactionProcessor;
        this.transactionExecutionService = transactionExecutionService;
    }

    public Future<?> submitTransaction(TransactionRequest request) {
        BaseTransaction transaction = createTransactionFromRequest(request);
        TransactionCommand command = new DefaultTransactionCommand(transaction, transactionExecutionService);
        return asyncTransactionProcessor.submit(command);
    }

    private BaseTransaction createTransactionFromRequest(TransactionRequest request) {
        UUID transactionId = UUID.randomUUID();
        Instant timestamp = Instant.now();

        TransactionType type = request.getType();
        UUID userId = request.getUserId();
        UUID sourceAccountId = request.getSourceAccountId();
        UUID targetAccountId = request.getTargetAccountId();
        BigDecimal amount = request.getAmount();

        if (type == TransactionType.DEPOSIT) {
            return new DepositTransaction(transactionId, timestamp, userId, targetAccountId, amount);
        } else if (type == TransactionType.WITHDRAW) {
            return new WithdrawTransaction(transactionId, timestamp, userId, sourceAccountId, amount);
        } else if (type == TransactionType.FREEZE) {
            return new FreezeTransaction(transactionId, timestamp, userId, targetAccountId);
        } else if (type == TransactionType.TRANSFER) {
            return new TransferTransaction(transactionId, timestamp, userId, sourceAccountId, targetAccountId, amount);
        } else {
            throw new IllegalArgumentException("Unsupported transaction type " + type);
        }
    }
}
