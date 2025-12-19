package bsu.famcs.nikonchik.lab2.backend.factories;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.entities.events.Event.EventStatus;
import org.springframework.stereotype.Component;

import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.*;
import bsu.famcs.nikonchik.lab2.backend.exceptions.AccountExceptions.InvalidAmountException;

@Component
public class TransactionFactory {
    public TransferTransaction createTransfer(UUID initiatorId, UUID fromAccount,
                                              UUID toAccount, BigDecimal amount,
                                              String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        return new TransferTransaction(
                UUID.randomUUID(),
                initiatorId,
                amount,
                LocalDateTime.now(),
                EventStatus.PENDING,
                description,
                fromAccount,
                toAccount
        );
    }

    public DepositTransaction createDeposit(UUID initiatorId, UUID toAccount,
                                            BigDecimal amount, String description,
                                            String depositMethod) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        return new DepositTransaction(
                UUID.randomUUID(),
                initiatorId,
                amount,
                LocalDateTime.now(),
                EventStatus.PENDING,
                description,
                toAccount,
                depositMethod
        );
    }

    public WithdrawalTransaction createWithdrawal(UUID initiatorId, UUID fromAccount,
                                                  BigDecimal amount, String description,
                                                  String location) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        return new WithdrawalTransaction(
                UUID.randomUUID(),
                initiatorId,
                amount,
                LocalDateTime.now(),
                EventStatus.PENDING,
                description,
                fromAccount,
                location
        );
    }
}
