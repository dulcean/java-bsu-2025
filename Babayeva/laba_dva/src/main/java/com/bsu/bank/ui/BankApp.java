package com.bsu.bank.ui; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL; 
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BankApp extends JFrame {

    private static final Color MINECRAFT_COLOR = new Color(158, 123, 87);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(231, 76, 60);
    private static final DecimalFormat DF = new DecimalFormat("0.00");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private static final String GIF_MAIN_PATH = "/com/bsu/bank/ui/assets/main.gif";
    private static final String GIF_REJECT_PATH = "/com/bsu/bank/ui/assets/reject.gif";
    private static final String PANEL_PATH = "/com/bsu/bank/ui/assets/trade_panel.png";
    private static final String BUTTON_DEPOSIT_PATH = "/com/bsu/bank/ui/assets/button_deposit.png";
    private static final String BUTTON_WITHDRAW_PATH = "/com/bsu/bank/ui/assets/button_withdraw.png";
    private static final String BUTTON_TRANSFER_PATH = "/com/bsu/bank/ui/assets/button_transfer.png";
    private static final String BUTTON_FREEZE_PATH = "/com/bsu/bank/ui/assets/button_freeze.png";
    private static final String BUTTON_UNFREEZE_PATH = "/com/bsu/bank/ui/assets/button_unfreeze.png";
    private static final String WINDOW_PATH = "/com/bsu/bank/ui/assets/window.png";
    private static final String AMOUNT_FIELD_PATH = "/com/bsu/bank/ui/assets/w.png";
    private static final String FONT_PATH = "/com/bsu/bank/ui/assets/minecraft.ttf";
    
    private final ImageIcon mainGif;
    private final ImageIcon rejectGif;
    private final ImageIcon panelIcon;
    private final ImageIcon depositButtonIcon;
    private final ImageIcon withdrawButtonIcon;
    private final ImageIcon transferButtonIcon;
    private final ImageIcon freezeButtonIcon;
    private final ImageIcon unfreezeButtonIcon;
    private final ImageIcon windowIcon;
    private final ImageIcon amountFieldIcon;
    private Font minecraftFont;

    private final User currentUser;
    private final Map<String, Account> accounts;
    private Account currentAccount;
    private final List<Transaction> transactionHistory;

    private JLabel balanceLabel;
    private JLabel statusLabel;
    private JComboBox<String> accountSelector;
    private JTextField amountField; 

    private JButton freezeButton;
    private JButton unfreezeButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton historyButton;

    private DefaultTableModel historyTableModel;
    
    private JPanel mainPanel; 
    private JLabel backgroundLabel;

    private static class Account {
        String id;
        String name;
        double balance;
        boolean isFrozen;

        public Account(String id, String name, double initialBalance) {
            this.id = id;
            this.name = name;
            this.balance = initialBalance;
            this.isFrozen = false;
        }

        public String getName() { return name; }
    }

    private static class Transaction {
        UUID uuid;
        LocalDateTime timestamp;
        String type;
        double amount;
        String accountId;
        String details;

        public Transaction(String type, double amount, String accountId, String details) {
            this.uuid = UUID.randomUUID();
            this.timestamp = LocalDateTime.now();
            this.type = type;
            this.amount = amount;
            this.accountId = accountId;
            this.details = details;
        }
    }

    private static class User {
        String uuid;
        String name;

        public User(String uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }
    }

    private enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER, FREEZE, UNFREEZE
    }

    public BankApp() {
        this.mainGif = loadImageResource(GIF_MAIN_PATH);
        this.rejectGif = loadImageResource(GIF_REJECT_PATH);
        this.panelIcon = loadImageResource(PANEL_PATH);
        this.depositButtonIcon = loadImageResource(BUTTON_DEPOSIT_PATH);
        this.withdrawButtonIcon = loadImageResource(BUTTON_WITHDRAW_PATH);
        this.transferButtonIcon = loadImageResource(BUTTON_TRANSFER_PATH);
        this.freezeButtonIcon = loadImageResource(BUTTON_FREEZE_PATH);
        this.unfreezeButtonIcon = loadImageResource(BUTTON_UNFREEZE_PATH);
        this.windowIcon = loadImageResource(WINDOW_PATH);
        this.amountFieldIcon = loadImageResource(AMOUNT_FIELD_PATH);
        this.minecraftFont = loadFontResource(FONT_PATH);
        
        currentUser = new User("9697433e-cf14-4b1e-9717-0727a377bd43", "Dulcean");
        accounts = new LinkedHashMap<>();
        accounts.put("Main-01", new Account("Main-01", "Основной Счет", 1500.00));
        accounts.put("Savings-02", new Account("Savings-02", "Сберегательный Счет", 5000.00));
        accounts.put("Credit-03", new Account("Credit-03", "Кредитный Счет", -200.00));
        currentAccount = accounts.get("Main-01"); 
        transactionHistory = new ArrayList<>();

        setTitle("Bank APP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
setSize(1200, 800);
        getContentPane().setBackground(MINECRAFT_COLOR);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setVerticalAlignment(JLabel.CENTER);
        if (mainGif != null) {
            backgroundLabel.setIcon(mainGif);
        }
        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        JPanel uiContainer = new JPanel(new BorderLayout(0, 0));
        uiContainer.setOpaque(false);
        uiContainer.add(createUserHeaderPanel(), BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        centerPanel.add(createRightButtonPanel(), BorderLayout.EAST);
        
        centerPanel.add(createHistoryButtonPanel(), BorderLayout.SOUTH);
        
        uiContainer.add(centerPanel, BorderLayout.CENTER);
        
        backgroundLabel.add(uiContainer, BorderLayout.CENTER);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);

        updateUI();
        updateTransactionHistoryTable();
    }
    
    private ImageIcon loadImageResource(String path) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        return null;
    }

    private Font loadFontResource(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
                return font.deriveFont(14f);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return new Font("SansSerif", Font.PLAIN, 14);
    }

private JPanel createRightButtonPanel() {
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setOpaque(false);
    
    rightPanel.setBorder(BorderFactory.createEmptyBorder());

    amountField = new JTextField("100.00");
    amountField.setMaximumSize(new Dimension(140, 25));
    amountField.setFont(minecraftFont);
    amountField.setHorizontalAlignment(JTextField.CENTER);
    rightPanel.add(amountField);

    depositButton = createImageButton(depositButtonIcon, e -> handleTransaction(TransactionType.DEPOSIT));
    withdrawButton = createImageButton(withdrawButtonIcon, e -> handleTransaction(TransactionType.WITHDRAW));
    transferButton = createImageButton(transferButtonIcon, e -> showTransferDialog());
    freezeButton = createImageButton(freezeButtonIcon, e -> handleTransaction(TransactionType.FREEZE));
    unfreezeButton = createImageButton(unfreezeButtonIcon, e -> handleTransaction(TransactionType.UNFREEZE));

    rightPanel.add(depositButton);
    rightPanel.add(withdrawButton);
    rightPanel.add(transferButton);
    rightPanel.add(freezeButton);
    rightPanel.add(unfreezeButton);

    return rightPanel;
}
    private JPanel createHistoryButtonPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setOpaque(false);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        
        historyButton = createImageButton(windowIcon, e -> showHistoryWindow());
        historyButton.setPreferredSize(new Dimension(176, 142));
        
        historyPanel.add(historyButton, BorderLayout.WEST);
        return historyPanel;
    }

    private JButton createImageButton(ImageIcon icon, ActionListener action) {
        JButton button = new JButton(icon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }

    private void showHistoryWindow() {
        JFrame historyFrame = new JFrame("История Транзакций");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(533, 400);
        historyFrame.setLocationRelativeTo(this);
        historyFrame.getContentPane().setBackground(MINECRAFT_COLOR);
        
        
        JPanel historyPanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"UUID (Сокращ.)", "Время", "Тип", "Сумма", "Счет", "Детали"}; 
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable historyTable = new JTable(historyTableModel);
        historyTable.setFont(minecraftFont.deriveFont(12f));
        historyTable.getTableHeader().setFont(minecraftFont.deriveFont(Font.BOLD, 12f));

        historyTable.getColumnModel().getColumn(0).setPreferredWidth(80); 
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(120); 
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(80);  
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(80);  
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(80);  
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(150); 

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(MINECRAFT_COLOR);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.setBackground(MINECRAFT_COLOR);
        
        historyFrame.add(historyPanel);
        historyFrame.setVisible(true);
        
        updateTransactionHistoryTable();
    }

    private JPanel createUserHeaderPanel() {
        JPanel panel = new TranslucentPanel(0); 
        panel.setOpaque(false); 
        
        JPanel userInfo = new JPanel(new GridLayout(3, 1));
        userInfo.setOpaque(false); 
        
        JLabel nameLabel = new JLabel("Пользователь: " + currentUser.name, SwingConstants.LEFT);
        nameLabel.setFont(minecraftFont.deriveFont(18f));
        nameLabel.setForeground(Color.WHITE); 
        
        JLabel uuidLabel = new JLabel("UUID: " + currentUser.uuid, SwingConstants.LEFT);
        uuidLabel.setFont(minecraftFont.deriveFont(14f));
        uuidLabel.setForeground(Color.LIGHT_GRAY); 

        accountSelector = new JComboBox<>(accounts.keySet().toArray(new String[0]));
        accountSelector.setBackground(MINECRAFT_COLOR);
        accountSelector.setFont(minecraftFont.deriveFont(14f));
        accountSelector.addActionListener(e -> {
            currentAccount = accounts.get(Objects.requireNonNull(accountSelector.getSelectedItem()).toString());
            updateUI();
            updateTransactionHistoryTable();
        });

        JPanel accountSelectorPanel = new JPanel(new BorderLayout(5, 0));
        accountSelectorPanel.setOpaque(false); 
        JLabel currentAccountLabel = new JLabel("Текущий счет: ");
        currentAccountLabel.setFont(minecraftFont.deriveFont(14f));
        currentAccountLabel.setForeground(Color.WHITE); 
        accountSelectorPanel.add(currentAccountLabel, BorderLayout.WEST);
        accountSelectorPanel.add(accountSelector, BorderLayout.CENTER);

        userInfo.add(nameLabel);
        userInfo.add(uuidLabel);
        userInfo.add(accountSelectorPanel);

        panel.add(userInfo, BorderLayout.WEST);

        JPanel statusInfo = new JPanel(new GridLayout(2, 1));
        statusInfo.setOpaque(false); 

        balanceLabel = new JLabel("Баланс: " + DF.format(currentAccount.balance) + " $", SwingConstants.RIGHT);
        balanceLabel.setFont(minecraftFont.deriveFont(20f));
        balanceLabel.setForeground(ACCENT_COLOR); 

        statusLabel = new JLabel("Статус: Активен", SwingConstants.RIGHT);
        statusLabel.setFont(minecraftFont.deriveFont(14f));
        statusLabel.setForeground(Color.WHITE); 

        statusInfo.add(balanceLabel);
        statusInfo.add(statusLabel);

        panel.add(statusInfo, BorderLayout.EAST);

        return panel;
    }

    private class TranslucentPanel extends JPanel {
        private final int alpha;

        public TranslucentPanel(int alpha) {
            this.alpha = alpha;
            setLayout(new BorderLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    private void updateUI() {
        double displayBalance = Math.round(currentAccount.balance * 100.0) / 100.0;
        balanceLabel.setText("Баланс: " + DF.format(displayBalance) + " $");

        if (currentAccount.isFrozen) {
            statusLabel.setText("Статус: СЧЕТ ЗАМОРОЖЕН");
            statusLabel.setForeground(WARNING_COLOR);
        } else {
            statusLabel.setText("Статус: СЧЕТ АКТИВЕН");
            statusLabel.setForeground(ACCENT_COLOR);
        }
        
        setRejectMode(false);
    }

    private void updateTransactionHistoryTable() {
        if (historyTableModel != null) {
            historyTableModel.setRowCount(0);

            transactionHistory.stream()
                    .sorted(Comparator.comparing(t -> t.timestamp, Comparator.reverseOrder()))
                    .forEach(t -> historyTableModel.addRow(new Object[]{
                        t.uuid.toString().substring(0, 8) + "...",
                        t.timestamp.format(DTF),
                        t.type,
                        DF.format(t.amount) + " $",
                        t.accountId,
                        t.details
                    }));
        }
    }

    private void showTransferDialog() {
        if (currentAccount.isFrozen) {
            triggerTemporaryRejectAnimation();
            return;
        }

        String[] targetAccountIds = accounts.keySet().stream()
                                          .filter(id -> !id.equals(currentAccount.id))
                                          .toArray(String[]::new);

        if (targetAccountIds.length == 0) {
            return;
        }

        String[] targetDisplayNames = Arrays.stream(targetAccountIds)
                                            .map(id -> id + " (" + accounts.get(id).getName() + ")")
                                            .toArray(String[]::new);

        JComboBox<String> transferTargetSelector = new JComboBox<>(targetDisplayNames);
        transferTargetSelector.setBackground(MINECRAFT_COLOR);
        transferTargetSelector.setFont(minecraftFont.deriveFont(14f));
        JTextField transferAmountField = new JTextField("100.00", 10);
        transferAmountField.setBackground(MINECRAFT_COLOR);
        transferAmountField.setFont(minecraftFont.deriveFont(14f));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBackground(MINECRAFT_COLOR);
        JLabel targetLabel = new JLabel("Счет-получатель:");
        targetLabel.setFont(minecraftFont.deriveFont(14f));
        JLabel amountLabel = new JLabel("Сумма трансфера:");
        amountLabel.setFont(minecraftFont.deriveFont(14f));
        
        panel.add(targetLabel);
        panel.add(transferTargetSelector);
        panel.add(amountLabel);
        panel.add(transferAmountField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Выполнить Трансфер со счета " + currentAccount.id,
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(transferAmountField.getText());
                String selectedDisplayName = Objects.requireNonNull(transferTargetSelector.getSelectedItem()).toString();
                String targetId = selectedDisplayName.split(" ")[0];

                if (amount <= 0) {
                    return;
                }

                handleTransfer(amount, targetId);
            } catch (NumberFormatException e) {
            } catch (Exception e) {
            }
        }
    }

    private void handleTransaction(TransactionType type) {
        double amount = 0.0;
        final double FEE = 5.00;
        
        boolean shouldShowReject = false;

        if (type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAW) {
            try {
                amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                return;
            }
        }

        try {
            switch (type) {
                case DEPOSIT:
                    if (currentAccount.isFrozen) { 
                        shouldShowReject = true;
                        break; 
                    }
                    currentAccount.balance += amount;
                    recordTransaction(type.name(), amount, "Пополнение счета."); 
                    break;

                case WITHDRAW:
                    if (currentAccount.isFrozen) { 
                        shouldShowReject = true;
                        break; 
                    }
                    if (currentAccount.balance < amount) { 
                        shouldShowReject = true;
                        break; 
                    }
                    currentAccount.balance -= amount;
                    recordTransaction(type.name(), -amount, "Снятие средств."); 
                    break;

                case FREEZE:
                    if (currentAccount.isFrozen) { 
                        break; 
                    }
                    if (currentAccount.balance < FEE) { 
                        shouldShowReject = true;
                        break; 
                    }
                    currentAccount.balance -= FEE;
                    currentAccount.isFrozen = true;
                    recordTransaction(type.name(), -FEE, "Комиссия за заморозку."); 
                    break;

                case UNFREEZE:
                    if (!currentAccount.isFrozen) { 
                        break; 
                    }
                    if (currentAccount.balance < FEE) { 
                        shouldShowReject = true;
                        break; 
                    }
                    currentAccount.balance -= FEE;
                    currentAccount.isFrozen = false;
                    recordTransaction(type.name(), -FEE, "Комиссия за разморозку."); 
                    break;
            }

            if (shouldShowReject) {
                triggerTemporaryRejectAnimation();
            }

            updateUI(); 
            updateTransactionHistoryTable(); 

        } catch (Exception ex) {
            triggerTemporaryRejectAnimation();
        }
    }

    private void handleTransfer(double amount, String targetId) {
        Account targetAccount = accounts.get(targetId);
        boolean shouldShowReject = false;

        try {
            if (targetAccount == null) { 
                shouldShowReject = true; 
                return; 
            }
            if (currentAccount.isFrozen) { 
                shouldShowReject = true; 
                return; 
            }
            if (targetAccount.isFrozen) { 
                shouldShowReject = true; 
                return; 
            }
            if (currentAccount.balance < amount) { 
                shouldShowReject = true; 
                return; 
            }

            currentAccount.balance -= amount;
            recordTransaction(TransactionType.TRANSFER.name() + "_OUT", -amount, "Трансфер на счет: " + targetId);

            targetAccount.balance += amount;
            transactionHistory.add(new Transaction(TransactionType.TRANSFER.name() + "_IN", amount, targetId,
                                                  "Получено со счета: " + currentAccount.id));

        } catch (Exception e) {
            shouldShowReject = true;
        } finally {
            if (shouldShowReject) {
                triggerTemporaryRejectAnimation();
            }
            updateUI();
            updateTransactionHistoryTable(); 
        }
    }
    
    private void setRejectMode(boolean reject) {
        if (backgroundLabel != null) {
            ImageIcon icon = reject ? rejectGif : mainGif;
            if (icon != null) {
                backgroundLabel.setIcon(icon);
                backgroundLabel.revalidate();
                backgroundLabel.repaint();
            }
        }
    }

    private void triggerTemporaryRejectAnimation() {
        setRejectMode(true);
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
            setRejectMode(false);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void recordTransaction(String type, double amount, String details) {
        transactionHistory.add(new Transaction(type, amount, currentAccount.id, details));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApp().setVisible(true));
    }
}