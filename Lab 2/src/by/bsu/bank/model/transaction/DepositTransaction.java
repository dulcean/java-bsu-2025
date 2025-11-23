package by.bsu.bank.model.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class DepositTransaction extends BaseTransaction {
    public DepositTransaction(UUID transactionId,
                              Instant timestamp,
                              UUID userId,
                              UUID targetAccountId,
                              BigDecimal amount) {
        super(transactionId, timestamp, TransactionType.DEPOSIT, userId, null, targetAccountId, amount);
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visitDepositTransaction(this);
    }
}
