package com.bsu.bank.main;

import com.bsu.bank.command.DepositCommand;
import com.bsu.bank.command.FeeCalculatorVisitor;
import com.bsu.bank.command.FreezeCommand;
import com.bsu.bank.command.TransactionCommand;
import com.bsu.bank.command.WithdrawCommand;
import com.bsu.bank.factory.TransactionFactory;
import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.model.TransactionAction;
import com.bsu.bank.model.User;
import com.bsu.bank.processor.TransactionProcessor;
import com.bsu.bank.repository.AccountRepository;

public class BankSystem {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Bank Transaction System Initialization ---");
        
        //Singleton
        AccountRepository repository = AccountRepository.getInstance();
        TransactionProcessor processor = new TransactionProcessor(repository);
        
        User user1 = new User("Paimon");
        Account account1 = new Account(1000.00);
        Account account2 = new Account(500.00);
        user1.addAccount(account1);
        user1.addAccount(account2);
        
        repository.save(account1);
        repository.save(account2);
        
        System.out.println(user1);
        System.out.println("Initial State:");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println("----------------------------------------------");

        // =================================================================
        // Демонстрация Атомарности и Асинхронности
        // =================================================================
        
        Transaction t1 = new Transaction(TransactionAction.DEPOSIT, 200.00, account1.getAccountId(), user1.getUserId());
        Transaction t2 = new Transaction(TransactionAction.WITHDRAW, 150.00, account1.getAccountId(), user1.getUserId());
        Transaction t3 = new Transaction(TransactionAction.FREEZE, 0.00, account2.getAccountId(), user1.getUserId());
        Transaction t4 = new Transaction(TransactionAction.WITHDRAW, 100.00, account2.getAccountId(), user1.getUserId());
                
        System.out.println("--- Submitting Transactions for Async Processing ---");
        
        processor.submitTransaction(TransactionFactory.createCommand(t1));
        processor.submitTransaction(TransactionFactory.createCommand(t2));
        processor.submitTransaction(TransactionFactory.createCommand(t3));
        processor.submitTransaction(TransactionFactory.createCommand(t4));

        System.out.println("\n--- Waiting for all transactions to complete (1s)... ---");
        Thread.sleep(1000); 

        System.out.println("\n--- Final State Check ---");
        System.out.println(repository.findAccountById(account1.getAccountId())); 
        System.out.println(repository.findAccountById(account2.getAccountId())); 
        
        // =================================================================
        // Демонстрация Visitor Pattern (Расчет комиссии)
        // =================================================================
        System.out.println("\n--- Visitor Pattern (Fee Calculation) ---");
        
        FeeCalculatorVisitor feeVisitor = new FeeCalculatorVisitor();
        
        TransactionCommand cmd1 = new DepositCommand(t1);
        TransactionCommand cmd2 = new WithdrawCommand(t2);
        TransactionCommand cmd3 = new FreezeCommand(t3);
        
        cmd1.accept(feeVisitor);
        cmd2.accept(feeVisitor);
        cmd3.accept(feeVisitor);

        System.out.printf("Total calculated fee for the processed transactions: $%.2f%n", feeVisitor.getTotalFee());
        System.out.println("----------------------------------------------");

        processor.shutdown();
    }
}
