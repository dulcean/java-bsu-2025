package com.bsu.bank.command;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@DisplayName("FreezeCommand")
class FreezeCommandTest {

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
    @DisplayName("Should successfully freeze unfrozen account")
    void execute_successfulFreeze() {
        assertFalse(account.isFrozen(), "Account should start unfrozen");
        Transaction transaction = new Transaction(TransactionAction.FREEZE, 0.0, account.getAccountId(), userId);
        FreezeCommand command = new FreezeCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertTrue(result, "Freeze should succeed");
        assertTrue(account.isFrozen(), "Account must be frozen after command");
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(true), contains("successfully frozen"));
    }

    @Test
    @DisplayName("Should fail if account is already frozen")
    void execute_alreadyFrozen() {
        account.setFrozen(true);
        assertTrue(account.isFrozen(), "Account should start frozen");
        Transaction transaction = new Transaction(TransactionAction.FREEZE, 0.0, account.getAccountId(), userId);
        FreezeCommand command = new FreezeCommand(transaction);

        boolean result = command.execute(mockRepository, List.of(mockObserver));

        assertFalse(result, "Freeze should fail");
        assertTrue(account.isFrozen(), "Account must remain frozen");
        verify(mockObserver, times(1)).onTransactionCompleted(eq(transaction), eq(false), contains("already frozen"));
    }
}