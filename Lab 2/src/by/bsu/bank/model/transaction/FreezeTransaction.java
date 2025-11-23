package by.bsu.bank.model.transaction;

import java.time.Instant;
import java.util.UUID;

public class FreezeTransaction extends BaseTransaction {
    public FreezeTransaction(UUID transactionId,
                             Instant timestamp,
                             UUID userId,
                             UUID targetAccountId) {
        super(transactionId, timestamp, TransactionType.FREEZE, userId, null, targetAccountId, null);
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visitFreezeTransaction(this);
    }
}
