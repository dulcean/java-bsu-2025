package bank;

import bank.async.AsyncTransactionProcessor;
import bank.gui.BankGUI;
import bank.service.AccountService;
import bank.service.TransactionService;
import bank.service.UserService;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();

        AsyncTransactionProcessor processor = new AsyncTransactionProcessor(transactionService, 4);

        SwingUtilities.invokeLater(() -> {
            new BankGUI(accountService, transactionService, userService, processor);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(processor::shutdown));
    }
}
