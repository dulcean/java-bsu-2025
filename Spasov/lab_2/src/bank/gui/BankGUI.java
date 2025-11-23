package bank.gui;

import bank.async.AsyncTransactionProcessor;
import bank.model.Account;
import bank.model.Transaction;
import bank.model.User;
import bank.model.enums.TransactionType;
import bank.observer.ConsoleNotifier;
import bank.service.AccountService;
import bank.service.TransactionService;
import bank.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BankGUI extends JFrame {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final UserService userService;

    private final AsyncTransactionProcessor processor;

    private final DefaultTableModel userTableModel;
    private final DefaultTableModel accountTableModel;
    private final DefaultTableModel transactionTableModel;

    public BankGUI(AccountService accountService,
                   TransactionService transactionService,
                   UserService userService,
                   AsyncTransactionProcessor processor) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.processor = processor;

        processor.addListener(new ConsoleNotifier());
        processor.addListener(tx -> {
            SwingUtilities.invokeLater(() -> {
                updateTransactionsTable(tx);
                updateAccountsTable(accountService.find(tx.getAccountId()).orElse(null));

                if (tx.getTargetAccountId() != null) {
                    updateAccountsTable(accountService.find(tx.getTargetAccountId()).orElse(null));
                }
            });
        });

        setTitle("Bank System GUI");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font bigFont = new Font("Arial", Font.BOLD, 16);

        JPanel northPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel userPanel = new JPanel();
        userPanel.setBorder(BorderFactory.createTitledBorder("Add User"));
        JTextField nicknameField = new JTextField(15);
        JButton createUserButton = new JButton("Create User");
        nicknameField.setFont(bigFont);
        createUserButton.setFont(bigFont);
        userPanel.add(new JLabel("Nickname:"));
        userPanel.add(nicknameField);
        userPanel.add(createUserButton);
        northPanel.add(userPanel);

        JPanel accountPanel = new JPanel();
        accountPanel.setBorder(BorderFactory.createTitledBorder("Add Account"));
        JTextField userIdField = new JTextField(15);
        JTextField initialBalanceField = new JTextField(15);
        JButton createAccountButton = new JButton("Create Account");
        userIdField.setFont(bigFont);
        initialBalanceField.setFont(bigFont);
        createAccountButton.setFont(bigFont);
        accountPanel.add(new JLabel("User ID:"));
        accountPanel.add(userIdField);
        accountPanel.add(new JLabel("Initial Balance:"));
        accountPanel.add(initialBalanceField);
        accountPanel.add(createAccountButton);
        northPanel.add(accountPanel);

        add(northPanel, BorderLayout.NORTH);

        userTableModel = new DefaultTableModel(new Object[]{"User ID", "Nickname"}, 0);
        JTable userTable = new JTable(userTableModel);
        userTable.setFont(bigFont);
        userTable.setRowHeight(24);
        JScrollPane userScroll = new JScrollPane(userTable);
        userScroll.setBorder(BorderFactory.createTitledBorder("Users Table"));
        add(userScroll, BorderLayout.WEST);

        accountTableModel = new DefaultTableModel(
                new Object[]{"Account ID", "User ID", "Balance", "Status"}, 0);
        JTable accountTable = new JTable(accountTableModel);
        accountTable.setFont(bigFont);
        accountTable.setRowHeight(24);
        JScrollPane accountScroll = new JScrollPane(accountTable);
        accountScroll.setBorder(BorderFactory.createTitledBorder("Accounts Table"));
        add(accountScroll, BorderLayout.EAST);

        JPanel transactionPanel = new JPanel();
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Execute Transaction"));
        JTextField accountIdField = new JTextField(12);
        JTextField targetAccountIdField = new JTextField(12);
        JTextField amountField = new JTextField(12);
        JComboBox<TransactionType> typeCombo = new JComboBox<>(TransactionType.values());
        JButton executeTxButton = new JButton("Execute Transaction");
        accountIdField.setFont(bigFont);
        targetAccountIdField.setFont(bigFont);
        amountField.setFont(bigFont);
        typeCombo.setFont(bigFont);
        executeTxButton.setFont(bigFont);
        transactionPanel.add(new JLabel("Account ID:"));
        transactionPanel.add(accountIdField);
        transactionPanel.add(new JLabel("Target ID:"));
        transactionPanel.add(targetAccountIdField);
        transactionPanel.add(new JLabel("Amount:"));
        transactionPanel.add(amountField);
        transactionPanel.add(typeCombo);
        transactionPanel.add(executeTxButton);
        add(transactionPanel, BorderLayout.SOUTH);

        transactionTableModel = new DefaultTableModel(
                new Object[]{"Transaction ID", "Type", "Amount", "Account", "Target", "Status"}, 0);
        JTable txTable = new JTable(transactionTableModel);
        txTable.setFont(bigFont);
        txTable.setRowHeight(24);
        JScrollPane txScroll = new JScrollPane(txTable);
        txScroll.setBorder(BorderFactory.createTitledBorder("Transactions Table"));
        add(txScroll, BorderLayout.CENTER);

        createUserButton.addActionListener(e -> {
            try {
                String nickname = nicknameField.getText().trim();
                if (nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nickname cannot be empty!");
                    return;
                }
                User user = userService.createUser(nickname);
                userTableModel.addRow(new Object[]{user.getId(), user.getNickname()});
                JOptionPane.showMessageDialog(this, "User created: " + user.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        createAccountButton.addActionListener(e -> {
            try {
                UUID userId = UUID.fromString(userIdField.getText().trim());
                BigDecimal balance = new BigDecimal(initialBalanceField.getText().trim());
                Account account = accountService.create(userId, balance);
                accountTableModel.addRow(new Object[]{account.getId(), account.getUserId(), account.getBalance(), account.getStatus()});
                JOptionPane.showMessageDialog(this, "Account created: " + account.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        executeTxButton.addActionListener(e -> {
            try {
                UUID accountId = UUID.fromString(accountIdField.getText().trim());
                UUID targetId = targetAccountIdField.getText().trim().isEmpty() ? null : UUID.fromString(targetAccountIdField.getText().trim());
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                TransactionType type = (TransactionType) typeCombo.getSelectedItem();

                Transaction tx = new Transaction(UUID.randomUUID(), accountId, targetId, amount, LocalDateTime.now(), type);

                processor.submitTransaction(tx);

                JOptionPane.showMessageDialog(this, "Transaction submitted: " + tx.getId());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        loadAllUsers();
        loadAllAccounts();
        loadAllTransactions();

        setVisible(true);
    }

    private void updateAccountsTable(Account account) {
        if (account == null) return;

        for (int i = 0; i < accountTableModel.getRowCount(); i++) {
            Object value = accountTableModel.getValueAt(i, 0);
            UUID rowId;

            if (value instanceof UUID) {
                rowId = (UUID) value;
            } else if (value instanceof String) {
                rowId = UUID.fromString((String) value);
            } else {
                continue;
            }

            if (rowId.equals(account.getId())) {
                accountTableModel.setValueAt(account.getBalance(), i, 2);
                accountTableModel.setValueAt(account.getStatus(), i, 3);
                return;
            }
        }
        accountTableModel.addRow(new Object[]{account.getId(), account.getUserId(), account.getBalance(), account.getStatus()});
    }

    private void updateTransactionsTable(Transaction tx) {
        transactionTableModel.addRow(new Object[]{
                tx.getId(),
                tx.getType(),
                tx.getAmount(),
                tx.getAccountId(),
                tx.getTargetAccountId(),
                tx.getStatus()
        });
    }

    private void loadAllUsers() {
        userService.getAllUsers().forEach(u -> userTableModel.addRow(new Object[]{u.getId(), u.getNickname()}));
    }

    private void loadAllAccounts() {
        accountService.getAllAccounts().forEach(this::updateAccountsTable);
    }

    private void loadAllTransactions() {
        transactionService.getAllTransactions().forEach(this::updateTransactionsTable);
    }
}
