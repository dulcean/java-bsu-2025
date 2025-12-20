package com.bsu.bank.repository;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bsu.bank.model.Account;

/**
 * Тесты для AccountRepository, проверяющие Singleton-поведение
 * и взаимодействие с базой данных H2.
 */
class AccountRepositoryTest {

    private AccountRepository repository;
    private final UUID testAccountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        AccountRepository.resetInstance();
        repository = AccountRepository.getInstance();
    }
    
    @AfterEach
    void tearDown() {
        repository.clearAll();
    }

    @Test
    void testSingletonInstance() {
        AccountRepository anotherInstance = AccountRepository.getInstance();
        assertSame(repository, anotherInstance, "AccountRepository must be a Singleton.");
    }

    @Test
    void testFindNonExistentAccount() {
        Account account = repository.findAccountById(UUID.randomUUID());
        assertNull(account, "Should return null for a non-existent account.");
    }

    @Test
    void testCreateAndFindAccount() {

        Account newAccount = new Account(testAccountId, 500.00, false);
        repository.createAccount(newAccount);

        Account foundAccount = repository.findAccountById(testAccountId);

        assertNotNull(foundAccount, "Account should be found after creation.");
        assertEquals(testAccountId, foundAccount.getAccountId());
        assertEquals(500.00, foundAccount.getBalance(), 0.001);
        assertFalse(foundAccount.isFrozen());
    }

    @Test
    void testSaveAndUpdateAccount() {
        Account initialAccount = new Account(testAccountId, 100.00, false);
        repository.createAccount(initialAccount);
        
        initialAccount.setBalance(250.75);
        initialAccount.setFrozen(true);
        repository.save(initialAccount);

        repository.clearAll();
        Account updatedAccount = repository.findAccountById(testAccountId);

        assertNotNull(updatedAccount);
        assertEquals(250.75, updatedAccount.getBalance(), 0.001, "Balance must be updated in DB.");
        assertTrue(updatedAccount.isFrozen(), "Frozen status must be updated in DB.");
    }
    
    @Test
    void testLockPreservationAfterCacheReload() {
        Account account1 = new Account(testAccountId, 100.0, false);
        repository.createAccount(account1);

        account1.getLock().lock();
        
        Account account2 = repository.findAccountById(testAccountId);

        assertNotNull(account2);
        assertSame(account1, account2, "Repository must return the same cached instance to preserve the lock.");
        
        assertTrue(account2.getLock().isHeldByCurrentThread(), "Lock must be held by the current thread on the same instance.");
        
        account1.getLock().unlock();
    }
}