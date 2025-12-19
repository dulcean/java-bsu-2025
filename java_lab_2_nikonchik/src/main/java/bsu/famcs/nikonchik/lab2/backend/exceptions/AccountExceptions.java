package bsu.famcs.nikonchik.lab2.backend.exceptions;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountExceptions {
    public static class AccountNotActiveException extends BankingException {
        public AccountNotActiveException(String message) {
            super(message);
        }

        public AccountNotActiveException(String accountNumber, String status) {
            super(String.format("Account %s is not active. Current status: %s", accountNumber, status));
        }
    }

    public static class InsufficientFundsException extends BankingException {
        public InsufficientFundsException(String message) {
            super(message);
        }

        public InsufficientFundsException(String accountNumber, BigDecimal balance,
                                          BigDecimal amount, String currency) {
            super(String.format(
                    "Insufficient funds in account %s. Balance: %s %s, requested: %s %s",
                    accountNumber, balance, currency, amount, currency
            ));
        }
    }

    public static class AccountNotFoundException extends BankingException {
        public AccountNotFoundException(String message) {
            super(message);
        }

        public AccountNotFoundException(UUID accountId) {
            super("Account not found with ID: " + accountId);
        }

        public AccountNotFoundException(String accountNumber, String details) {
            super("Account not found: " + accountNumber + ". " + details);
        }
    }

    public static class InvalidAmountException extends BankingException {
        public InvalidAmountException(String message) {
            super(message);
        }
    }
}

