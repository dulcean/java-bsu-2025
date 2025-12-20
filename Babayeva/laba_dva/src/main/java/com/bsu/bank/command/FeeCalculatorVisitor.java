package com.bsu.bank.command;

public class FeeCalculatorVisitor implements TransactionVisitor {
    private double totalFee = 0.0;
    
    public double getTotalFee() { return totalFee; }

    @Override
    public void visit(DepositCommand command) {
        totalFee += command.getTransaction().getAmount() * 0.001; 
    }

    @Override
    public void visit(WithdrawCommand command) {
        totalFee += 5.00; 
    }

    @Override
    public void visit(FreezeCommand command) {
    }
}