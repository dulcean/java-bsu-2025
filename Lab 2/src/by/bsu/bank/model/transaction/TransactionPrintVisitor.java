package by.bsu.bank.model.transaction;

import java.time.format.DateTimeFormatter;

public class TransactionPrintVisitor implements TransactionVisitor {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    @Override
    public void visitDepositTransaction(DepositTransaction transaction) {
        System.out.println("Deposit " + transaction.getAmount()
                + " to account " + transaction.getTargetAccountId()
                + " at " + formatter.format(transaction.getTimestamp()));
    }

    @Override
    public void visitWithdrawTransaction(WithdrawTransaction transaction) {
        System.out.println("Withdraw " + transaction.getAmount()
                + " from account " + transaction.getSourceAccountId()
                + " at " + formatter.format(transaction.getTimestamp()));
    }

    @Override
    public void visitFreezeTransaction(FreezeTransaction transaction) {
        System.out.println("Freeze account " + transaction.getTargetAccountId()
                + " at " + formatter.format(transaction.getTimestamp()));
    }

    @Override
    public void visitTransferTransaction(TransferTransaction transaction) {
        System.out.println("Transfer " + transaction.getAmount()
                + " from account " + transaction.getSourceAccountId()
                + " to account " + transaction.getTargetAccountId()
                + " at " + formatter.format(transaction.getTimestamp()));
    }
}
