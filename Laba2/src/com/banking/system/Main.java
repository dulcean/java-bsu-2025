package com.banking.system;

import com.banking.system.database.*;
import com.banking.system.events.NotificationSystem;
import com.banking.system.gui.MainWindow;
import com.banking.system.logic.AsyncExecutor;
import com.banking.system.logic.BankingService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        var db = DbConnection.getInstance();
        var conn = db.getConnection();

        var clientRepo = new ClientRepo(conn);
        var accountRepo = new AccountRepo(conn);
        var opRepo = new OperationRepo(conn);

        var notifications = new NotificationSystem();
        var service = new BankingService(accountRepo, opRepo, notifications);

        var executor = new AsyncExecutor(4, service);

        SwingUtilities.invokeLater(() -> {
            var win = new MainWindow(clientRepo, accountRepo, opRepo, executor);
            notifications.addListener(win);
            win.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.stop();
            db.close();
        }));
    }
}