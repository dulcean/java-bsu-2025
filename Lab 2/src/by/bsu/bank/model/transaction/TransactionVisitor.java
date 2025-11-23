package by.bsu.bank.model.transaction;

public interface TransactionVisitor {
    void visitDepositTransaction(DepositTransaction transaction);
    void visitWithdrawTransaction(WithdrawTransaction transaction);
    void visitFreezeTransaction(FreezeTransaction transaction);
    void visitTransferTransaction(TransferTransaction transaction);
}
