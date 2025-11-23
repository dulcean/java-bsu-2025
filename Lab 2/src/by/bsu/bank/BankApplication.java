package by.bsu.bank;

import by.bsu.bank.async.AsyncTransactionProcessor;
import by.bsu.bank.event.LoggingTransactionListener;
import by.bsu.bank.event.TransactionEventPublisher;
import by.bsu.bank.factory.TransactionStrategyFactory;
import by.bsu.bank.model.BankAccount;
import by.bsu.bank.model.BankUser;
import by.bsu.bank.model.transaction.TransactionType;
import by.bsu.bank.repository.InMemoryAccountRepository;
import by.bsu.bank.repository.InMemoryUserRepository;
import by.bsu.bank.service.TransactionExecutionService;
import by.bsu.bank.service.TransactionService;
import by.bsu.bank.service.dto.TransactionRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BankApplication {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();

        TransactionEventPublisher eventPublisher = new TransactionEventPublisher();
        eventPublisher.registerListener(new LoggingTransactionListener());

        TransactionStrategyFactory strategyFactory = new TransactionStrategyFactory(accountRepository);
        TransactionExecutionService executionService = new TransactionExecutionService(strategyFactory, eventPublisher);
        AsyncTransactionProcessor asyncProcessor = AsyncTransactionProcessor.getInstance();

        TransactionService transactionService = new TransactionService(asyncProcessor, executionService);

        UUID userId = UUID.randomUUID();
        BankAccount firstAccount = new BankAccount(UUID.randomUUID(), "ACC-1", new BigDecimal("1000.00"));
        BankAccount secondAccount = new BankAccount(UUID.randomUUID(), "ACC-2", new BigDecimal("500.00"));

        BankUser bankUser = new BankUser(userId, "Aether", Arrays.asList(firstAccount, secondAccount));
        userRepository.save(bankUser);

        accountRepository.save(firstAccount, userId);
        accountRepository.save(secondAccount, userId);

        TransactionRequest depositRequest = new TransactionRequest(
                TransactionType.DEPOSIT,
                userId,
                null,
                firstAccount.getAccountId(),
                new BigDecimal("200.00")
        );

        TransactionRequest withdrawRequest = new TransactionRequest(
                TransactionType.WITHDRAW,
                userId,
                firstAccount.getAccountId(),
                null,
                new BigDecimal("150.00")
        );

        TransactionRequest transferRequest = new TransactionRequest(
                TransactionType.TRANSFER,
                userId,
                firstAccount.getAccountId(),
                secondAccount.getAccountId(),
                new BigDecimal("300.00")
        );

        Future<?> depositFuture = transactionService.submitTransaction(depositRequest);
        Future<?> withdrawFuture = transactionService.submitTransaction(withdrawRequest);
        Future<?> transferFuture = transactionService.submitTransaction(transferRequest);

        depositFuture.get();
        withdrawFuture.get();
        transferFuture.get();

        System.out.println("First account balance: " + firstAccount.getBalance());
        System.out.println("Second account balance: " + secondAccount.getBalance());

        asyncProcessor.shutdown();
    }
}
