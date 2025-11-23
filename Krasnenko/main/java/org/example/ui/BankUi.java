package org.example.ui;

import org.example.model.*;
import org.example.repo.*;
import org.example.service.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class BankUi {

    private final InMemoryAccountRepository accounts = new InMemoryAccountRepository();
    private final AsyncTransactionProcessor processor;

    private JComboBox<UUID> accountBox;
    private JTextField amountField;
    private JTextField newAccountField;
    private JTextArea log;
    private JTextArea balanceArea;

    public BankUi() {
        InMemoryTransactionRepository txRepo = new InMemoryTransactionRepository();
        TransactionStrategyFactory factory = new TransactionStrategyFactory(accounts, txRepo);
        processor = new AsyncTransactionProcessor(factory);

        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame("Bank Transaction UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Select Account:"));
        accountBox = new JComboBox<>();
        topPanel.add(accountBox);

        topPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        topPanel.add(amountField);

        topPanel.add(new JLabel("New Account Balance:"));
        newAccountField = new JTextField();
        topPanel.add(newAccountField);

        JButton addAccountBtn = new JButton("Add Account");
        addAccountBtn.addActionListener(e -> addAccount());
        topPanel.add(addAccountBtn);

        JButton showBalanceBtn = new JButton("Show Balances");
        showBalanceBtn.addActionListener(e -> showBalances());
        topPanel.add(showBalanceBtn);

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton freezeBtn = new JButton("Freeze");

        depositBtn.addActionListener(e -> runTransaction(TransactionType.DEPOSIT));
        withdrawBtn.addActionListener(e -> runTransaction(TransactionType.WITHDRAW));
        freezeBtn.addActionListener(e -> runTransaction(TransactionType.FREEZE));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        btnPanel.add(depositBtn);
        btnPanel.add(withdrawBtn);
        btnPanel.add(freezeBtn);

        log = new JTextArea();
        log.setEditable(false);
        JScrollPane logScroll = new JScrollPane(log);
        logScroll.setBorder(BorderFactory.createTitledBorder("Transaction Log"));

        balanceArea = new JTextArea();
        balanceArea.setEditable(false);
        JScrollPane balanceScroll = new JScrollPane(balanceArea);
        balanceScroll.setBorder(BorderFactory.createTitledBorder("Balances"));
        balanceScroll.setPreferredSize(new Dimension(480, 150));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(btnPanel, BorderLayout.CENTER);
        frame.add(logScroll, BorderLayout.WEST);
        frame.add(balanceScroll, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addAccount() {
        try {
            BigDecimal amount = new BigDecimal(newAccountField.getText());
            UUID userId = UUID.randomUUID();
            UUID accId = UUID.randomUUID();
            Account acc = new Account(accId, userId, amount, "USD");
            accounts.add(acc);
            accountBox.addItem(accId);
            log.append("Added new account: " + accId + " with balance " + amount + "\n");
            newAccountField.setText("");
        } catch (NumberFormatException e) {
            log.append("Invalid amount for new account\n");
        }
    }

    private void showBalances() {
        StringBuilder sb = new StringBuilder();
        for (Account acc : accounts.getAll()) {
            sb.append(acc.getId()).append(" : ").append(acc.getBalance()).append("\n");
        }
        balanceArea.setText(sb.toString());
    }

    private void runTransaction(TransactionType type) {
        UUID accId = (UUID) accountBox.getSelectedItem();
        if (accId == null) {
            log.append("No account selected\n");
            return;
        }

        BigDecimal amount = BigDecimal.ZERO;
        try {
            if (!amountField.getText().isEmpty()) {
                amount = new BigDecimal(amountField.getText());
            }
        } catch (NumberFormatException ex) {
            log.append("Invalid amount\n");
            return;
        }

        TransactionRecord tx = new TransactionRecord(UUID.randomUUID(), Instant.now(), type, amount, accId, null, null);
        processor.submit(tx).thenAccept(success -> {
            log.append(type + " " + (success ? "succeeded" : "failed") + "\n");
            showBalances();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankUi::new);
    }
}