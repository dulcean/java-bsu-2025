package org.example.app;

import org.example.model.*;
import org.example.repo.*;
import org.example.service.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws Exception {
        InMemoryAccountRepository accounts = new InMemoryAccountRepository();
        InMemoryTransactionRepository txRepo = new InMemoryTransactionRepository();

        UUID userId = UUID.randomUUID();
        UUID acc1 = UUID.randomUUID();
        UUID acc2 = UUID.randomUUID();

        Account a1 = new Account(acc1, userId, new BigDecimal("1000.00"), "USD");
        Account a2 = new Account(acc2, userId, new BigDecimal("100.00"), "USD");
        accounts.add(a1);
        accounts.add(a2);

        TransactionStrategyFactory factory = new TransactionStrategyFactory(accounts, txRepo);
        AsyncTransactionProcessor processor = new AsyncTransactionProcessor(factory);

        TransactionRecord t1 = new TransactionRecord(UUID.randomUUID(), Instant.now(), TransactionType.DEPOSIT, new BigDecimal("50.00"), acc1, null, userId);
        CompletableFuture<Boolean> f1 = processor.submit(t1);

        TransactionRecord t2 = new TransactionRecord(UUID.randomUUID(), Instant.now(), TransactionType.WITHDRAW, new BigDecimal("20.00"), acc2, null, userId);
        CompletableFuture<Boolean> f2 = processor.submit(t2);

        TransactionRecord t3 = new TransactionRecord(UUID.randomUUID(), Instant.now(), TransactionType.TRANSFER, new BigDecimal("200.00"), acc1, acc2, userId);
        CompletableFuture<Boolean> f3 = processor.submit(t3);

        System.out.println("Waiting results...");
        System.out.println("deposit: " + f1.get());
        System.out.println("withdraw: " + f2.get());
        System.out.println("transfer: " + f3.get());

        System.out.println("Final balances:");
        System.out.println(accounts.findById(acc1).get());
        System.out.println(accounts.findById(acc2).get());

        processor.shutdown();
    }
}
