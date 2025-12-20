package com.bsu.bank.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bsu.bank.command.TransactionCommand;
import com.bsu.bank.observer.TransactionObserver;
import com.bsu.bank.repository.AccountRepository;

/**
 * Используем шаблон Observer для уведомления о завершении.
 */
public class TransactionProcessor {
    private final ExecutorService executorService;
    private final AccountRepository repository;
    
    private final List<TransactionObserver> observers = Collections.synchronizedList(new ArrayList<>());

    public TransactionProcessor(AccountRepository repository) {
        // Создаем фиксированный пул из 10 потоков
        this.executorService = Executors.newFixedThreadPool(10); 
        this.repository = repository;
        
        // Добавляем консольного наблюдателя по умолчанию
        addObserver(new ConsoleObserver()); 
    }

    /**
     * Ставит команду в очередь на выполнение.
     * @param command Команда для выполнения.
     */
    public void submitTransaction(TransactionCommand command) {
        // Задача, которая будет выполнена в пуле потоков
        executorService.submit(() -> {
            try {
                command.execute(repository, observers);
            } catch (Exception e) {
                System.err.printf("Processor error executing transaction [%s]: %s%n", 
                                  command.getTransaction().getTransactionId().toString().substring(0, 8), 
                                  e.getMessage());
                notifyObservers(false, "Unexpected processor error: " + e.getMessage());
            }
        });
    }

    public void addObserver(TransactionObserver observer) {
        this.observers.add(observer);
    }
    
    private static class ConsoleObserver implements TransactionObserver {
        @Override
        public void onTransactionCompleted(com.bsu.bank.model.Transaction transaction, boolean success, String message) {
            String status = success ? "SUCCESS" : "FAILURE";
            System.out.printf("[Observer] Transaction ID %s: Status: %s. Message: %s%n", 
                              transaction.getTransactionId().toString().substring(0, 8), status, message);
        }
    }
    
    private void notifyObservers(boolean success, String message) {
        observers.forEach(o -> System.err.printf("[System Observer] Status: %s. Message: %s%n", 
                                                success ? "SUCCESS" : "FAILURE", message));
    }
    
    public void shutdown() {
        executorService.shutdown();
        try {
            // Ждем завершения всех задач до 5 секунд
            if (!executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}