package by.bsu.bank.model;

import by.bsu.bank.exception.AccountFrozenException;
import by.bsu.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final UUID accountId;
    private final String accountNumber;
    private BigDecimal balance;
    private AccountStatus status;
    private final ReentrantLock accountLock;

    public BankAccount(UUID accountId, String accountNumber, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.accountLock = new ReentrantLock();
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        accountLock.lock();
        try {
            return balance;
        } finally {
            accountLock.unlock();
        }
    }

    public AccountStatus getStatus() {
        accountLock.lock();
        try {
            return status;
        } finally {
            accountLock.unlock();
        }
    }

    public ReentrantLock getAccountLock() {
        return accountLock;
    }

    public void deposit(BigDecimal amount) {
        accountLock.lock();
        try {
            if (status == AccountStatus.FROZEN) {
                throw new AccountFrozenException("Cannot deposit to frozen account " + accountNumber);
            }
            if (amount == null || amount.signum() <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            balance = balance.add(amount);
        } finally {
            accountLock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        accountLock.lock();
        try {
            if (status == AccountStatus.FROZEN) {
                throw new AccountFrozenException("Cannot withdraw from frozen account " + accountNumber);
            }
            if (amount == null || amount.signum() <= 0) {
                throw new IllegalArgumentException("Withdraw amount must be positive");
            }
            if (balance.compareTo(amount) < 0) {
                throw new InsufficientFundsException("Not enough money on account " + accountNumber);
            }
            balance = balance.subtract(amount);
        } finally {
            accountLock.unlock();
        }
    }

    public void freeze() {
        accountLock.lock();
        try {
            status = AccountStatus.FROZEN;
        } finally {
            accountLock.unlock();
        }
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
