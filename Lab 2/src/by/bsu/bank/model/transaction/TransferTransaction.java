package by.bsu.bank.model.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransferTransaction extends BaseTransaction {
    public TransferTransaction(UUID transactionId,
                               Instant timestamp,
                               UUID userId,
                               UUID sourceAccountId,
                               UUID targetAccountId,
                               BigDecimal amount) {
        super(transactionId, timestamp, TransactionType.TRANSFER, userId, sourceAccountId, targetAccountId, amount);
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visitTransferTransaction(this);
    }
}
