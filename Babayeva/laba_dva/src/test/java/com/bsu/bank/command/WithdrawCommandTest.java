package com.bsu.bank.command;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bsu.bank.model.Account;
import com.bsu.bank.model.Transaction;
import com.bsu.bank.model.TransactionAction;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

@DisplayName("WithdrawCommand")
class WithdrawCommandTest {

    private AccountRepository mockRepository;
    private TransactionObserver mockObserver;
    private Account account;
    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockRepository = mock(AccountRepository.class);
        mockObserver = mock(TransactionObserver.class);
        account = new Account(100.0);
        when(mockRepository.findAccountById(account.getAccountId())).thenReturn(account);
    }

    @Test
    @DisplayName("Should successfully withdraw funds and update balance")
    void execute_successfulWithdrawal() {
        Transaction transaction = new Transaction(TransactionAction.WITHDRAW, 50.0, account.getAccountId(), userId);
        WithdrawCommand command = new WithdrawCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertTrue(result, "Withdrawal should succeed");
        assertEquals(50.0, account.getBalance(), 0.01, "Balance should be updated");
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(true), anyString());
    }

    @Test
    @DisplayName("Should fail due to insufficient funds")
    void execute_insufficientFunds() {
        Transaction transaction = new Transaction(TransactionAction.WITHDRAW, 150.0, account.getAccountId(), userId);
        WithdrawCommand command = new WithdrawCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertFalse(result, "Withdrawal should fail");
        assertEquals(100.0, account.getBalance(), 0.01, "Balance should not change");
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), contains("Insufficient funds"));
    }

    @Test
    @DisplayName("Should fail if account is frozen")
    void execute_frozenAccount() {
        account.setFrozen(true);
        Transaction transaction = new Transaction(TransactionAction.WITHDRAW, 50.0, account.getAccountId(), userId);
        WithdrawCommand command = new WithdrawCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertFalse(result, "Withdrawal should fail for frozen account");
        assertEquals(100.0, account.getBalance(), 0.01, "Balance should not change");
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), contains("Account is frozen"));
    }
}