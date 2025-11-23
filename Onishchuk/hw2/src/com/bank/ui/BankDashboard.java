package com.bank.ui;

import com.bank.model.Account;
import com.bank.model.User;
import com.bank.observer.BankObserver;
import com.bank.repository.BankRepository;
import com.bank.service.BankSystem;
import com.bank.transaction.TransactionCommand;
import com.bank.transaction.TransactionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BankDashboard extends JFrame implements BankObserver {
    private final BankSystem bankSystem;
    private final TransactionFactory transactionFactory;
    private final List<User> users;

    private JComboBox<String> userSelector;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private User currentUser;

    public BankDashboard(List<User> users) {
        this.users = users;
        this.bankSystem = BankSystem.getInstance();
        this.transactionFactory = new TransactionFactory();
        this.bankSystem.addObserver(this);

        if (!users.isEmpty()) {
            this.currentUser = users.get(0);
        }

        setTitle("Bank System Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select User:"));

        userSelector = new JComboBox<>();
        for (User u : users) {
            userSelector.addItem(u.getNickname());
        }
        userSelector.addActionListener(e -> {
            int idx = userSelector.getSelectedIndex();
            if (idx >= 0) {
                currentUser = users.get(idx);
                refreshTable();
            }
        });
        topPanel.add(userSelector);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Account ID", "Balance", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        accountTable = new JTable(tableModel);
        add(new JScrollPane(accountTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnDeposit = new JButton("Deposit");
        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnTransfer = new JButton("Transfer");
        JButton btnFreeze = new JButton("Freeze");

        btnDeposit.addActionListener(e -> handleDeposit());
        btnWithdraw.addActionListener(e -> handleWithdraw());
        btnTransfer.addActionListener(e -> handleTransfer());
        btnFreeze.addActionListener(e -> handleFreeze());

        actionPanel.add(btnDeposit);
        actionPanel.add(btnWithdraw);
        actionPanel.add(btnTransfer);
        actionPanel.add(btnFreeze);
        add(actionPanel, BorderLayout.SOUTH);

        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Transaction Log"));
        add(logScroll, BorderLayout.EAST);
    }

    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            if (currentUser != null) {
                for (Account acc : currentUser.getAccounts()) {
                    tableModel.addRow(new Object[]{
                            acc.getId(),
                            acc.getBalance(),
                            acc.isFrozen() ? "FROZEN" : "ACTIVE"
                    });
                }
            }
        });
    }

    private UUID getSelectedAccountId() {
        int row = accountTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account first.");
            return null;
        }

        Object value = tableModel.getValueAt(row, 0);

        if (value instanceof String) {
            return UUID.fromString((String) value);
        }

        return (UUID) value;
    }

    private void handleDeposit() {
        UUID accId = getSelectedAccountId();
        if (accId == null) return;

        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null && !input.isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(input);
                bankSystem.submitTransaction(transactionFactory.createDeposit(accId, amount));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format");
            }
        }
    }

    private void handleWithdraw() {
        UUID accId = getSelectedAccountId();
        if (accId == null) return;

        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input != null && !input.isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(input);
                bankSystem.submitTransaction(transactionFactory.createWithdraw(accId, amount));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format");
            }
        }
    }

    private void handleFreeze() {
        UUID accId = getSelectedAccountId();
        if (accId == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to freeze this account?");
        if (confirm == JOptionPane.YES_OPTION) {
            bankSystem.submitTransaction(transactionFactory.createFreeze(accId));
        }
    }

    private void handleTransfer() {
        UUID fromId = getSelectedAccountId();
        if (fromId == null) return;

        String targetIdStr = JOptionPane.showInputDialog(this, "Enter target Account UUID:");
        if (targetIdStr == null || targetIdStr.isEmpty()) return;

        try {
            UUID toId = UUID.fromString(targetIdStr);
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
            if (amountStr != null) {
                BigDecimal amount = new BigDecimal(amountStr);
                bankSystem.submitTransaction(transactionFactory.createTransfer(fromId, toId, amount));
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid UUID or Amount");
        }
    }

    @Override
    public void onTransactionCompleted(TransactionCommand transaction) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(String.format("[%s] %s - %s\n",
                    transaction.getTimestamp(),
                    transaction.getClass().getSimpleName(),
                    transaction.getStatus()));
            refreshTable();
        });
    }
}