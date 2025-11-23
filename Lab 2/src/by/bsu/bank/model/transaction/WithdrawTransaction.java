package by.bsu.bank.model.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class WithdrawTransaction extends BaseTransaction {
    public WithdrawTransaction(UUID transactionId,
                               Instant timestamp,
                               UUID userId,
                               UUID sourceAccountId,
                               BigDecimal amount) {
        super(transactionId, timestamp, TransactionType.WITHDRAW, userId, sourceAccountId, null, amount);
    }

    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visitWithdrawTransaction(this);
    }
}
