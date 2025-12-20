package com.bsu.bank.command;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.model.TransactionAction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

@DisplayName("DepositCommand Tests")
class DepositCommandTest {

    private AccountRepository mockRepository;
    private TransactionObserver mockObserver;
    private Account account;
    private final UUID userId = UUID.randomUUID();
    private final double initialBalance = 100.0;
    private final double depositAmount = 50.0;

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(AccountRepository.class);
        mockObserver = Mockito.mock(TransactionObserver.class);
        
        account = new Account(initialBalance);
        
        Mockito.when(mockRepository.findAccountById(account.getAccountId())).thenReturn(account);
        
        account.setFrozen(false);
    }

    @Test
    @DisplayName("Should successfully deposit funds and update balance")
    void execute_successfulDeposit() {
        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, depositAmount, account.getAccountId(), userId);
        DepositCommand command = new DepositCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertTrue(result, "Deposit should succeed");
        assertEquals(initialBalance + depositAmount, account.getBalance(), 0.01, "Balance should be updated");
        
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(true), Mockito.contains("successful"));
        verify(mockRepository, times(1)).save(eq(account));
    }

    @Test
    @DisplayName("Should fail if account is frozen")
    void execute_frozenAccount() {
        account.setFrozen(true);
        
        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, depositAmount, account.getAccountId(), userId);
        DepositCommand command = new DepositCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertFalse(result, "Deposit should fail for frozen account");
        assertEquals(initialBalance, account.getBalance(), 0.01, "Balance should not change");
        
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), Mockito.contains("frozen"));
        verify(mockRepository, times(0)).save(Mockito.any(Account.class));
    }

    @Test
    @DisplayName("Should fail if account lock is unavailable")
    void execute_lockUnavailable() throws Exception {
        ReentrantLock lock = account.getLock();
        var executor = Executors.newSingleThreadExecutor();
        Future<Boolean> lockFuture = executor.submit(() -> {
            lock.lock();
            return true;
        });
        
        assertTrue(lockFuture.get(100, TimeUnit.MILLISECONDS), "The lock should be acquired by the background thread.");

        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, depositAmount, account.getAccountId(), userId);
        DepositCommand command = new DepositCommand(transaction);
        boolean result = command.execute(mockRepository, List.of(mockObserver));

        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        } else {
             try {

                if (lock.isLocked()) {
                    executor.submit(() -> lock.unlock()).get(100, TimeUnit.MILLISECONDS);
                }
             } catch (Exception ignored) { }
        }
        
        assertFalse(result, "Deposit should fail because lock is unavailable"); 
        assertEquals(initialBalance, account.getBalance(), 0.01, "Balance should not change");
        
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), Mockito.contains("lock"));
        verify(mockRepository, times(0)).save(Mockito.any(Account.class));
        
        executor.shutdownNow();
    }
    
    @Test
    @DisplayName("Should fail if account is not found")
    void execute_accountNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        
        Mockito.when(mockRepository.findAccountById(nonExistentId)).thenReturn(null);
        
        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, depositAmount, nonExistentId, userId);
        DepositCommand command = new DepositCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertFalse(result, "Deposit should fail if account is not found");
        
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), Mockito.contains("not found"));
        verify(mockRepository, times(0)).save(Mockito.any(Account.class));
    }
}