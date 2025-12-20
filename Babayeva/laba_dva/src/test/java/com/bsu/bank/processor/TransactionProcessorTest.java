package com.bsu.bank.processor;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bsu.bank.command.DepositCommand;
import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.model.TransactionAction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

public class TransactionProcessorTest {

    private AccountRepository repository;
    private TransactionProcessor processor;
    private Account testAccount;
    private final UUID testUserId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        AccountRepository.resetInstance(); 
        repository = AccountRepository.getInstance();
        
        testAccount = new Account(100.00);
        repository.createAccount(testAccount);
        
        processor = new TransactionProcessor(repository);
    }
    
    @AfterEach
    void tearDown() {
        processor.shutdown();
    }

    @Test
    void testSuccessfulDepositExecution() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        TransactionObserver testObserver = (transaction, success, message) -> {
            if (success) {
                latch.countDown();
            }
        };
        processor.addObserver(testObserver);

        Transaction transaction = new Transaction(
                TransactionAction.DEPOSIT, 50.00, testAccount.getAccountId(), testUserId
        );
        DepositCommand command = new DepositCommand(transaction);

        processor.submitTransaction(command);

        boolean finishedInTime = latch.await(5, TimeUnit.SECONDS);

        assertTrue(finishedInTime, "Transaction should complete within 5 seconds.");

        Account updatedAccount = repository.findAccountById(testAccount.getAccountId());
        assertNotNull(updatedAccount);
        assertEquals(150.00, updatedAccount.getBalance(), 0.001, "Balance should be updated correctly after deposit.");
    }
}