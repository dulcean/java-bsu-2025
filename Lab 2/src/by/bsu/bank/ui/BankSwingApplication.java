package by.bsu.bank.ui;

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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Future;

public class BankSwingApplication extends JFrame {
    private final TransactionService transactionService;
    private final AsyncTransactionProcessor asyncTransactionProcessor;
    private final BankAccount firstAccount;
    private final BankAccount secondAccount;
    private final UUID userId;

    private final JLabel firstAccountBalanceLabel;
    private final JLabel secondAccountBalanceLabel;
    private final JTextField depositAmountField;
    private final JTextField withdrawAmountField;
    private final JTextField transferAmountField;
    private final JTextArea logArea;

    public BankSwingApplication() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();

        TransactionEventPublisher eventPublisher = new TransactionEventPublisher();

        BankAccount createdFirstAccount = new BankAccount(UUID.randomUUID(), "ACC-1", new BigDecimal("1000.00"));
        BankAccount createdSecondAccount = new BankAccount(UUID.randomUUID(), "ACC-2", new BigDecimal("500.00"));
        this.firstAccount = createdFirstAccount;
        this.secondAccount = createdSecondAccount;

        UUID createdUserId = UUID.randomUUID();
        this.userId = createdUserId;
        BankUser bankUser = new BankUser(createdUserId, "Traveler", Arrays.asList(firstAccount, secondAccount));
        userRepository.save(bankUser);
        accountRepository.save(firstAccount, createdUserId);
        accountRepository.save(secondAccount, createdUserId);

        TransactionStrategyFactory strategyFactory = new TransactionStrategyFactory(accountRepository);
        TransactionExecutionService executionService = new TransactionExecutionService(strategyFactory, eventPublisher);
        this.asyncTransactionProcessor = AsyncTransactionProcessor.getInstance();
        this.transactionService = new TransactionService(asyncTransactionProcessor, executionService);

        this.firstAccountBalanceLabel = new JLabel("ACC-1 balance: " + firstAccount.getBalance());
        this.secondAccountBalanceLabel = new JLabel("ACC-2 balance: " + secondAccount.getBalance());
        this.depositAmountField = new JTextField();
        this.withdrawAmountField = new JTextField();
        this.transferAmountField = new JTextField();
        this.logArea = new JTextArea();
        logArea.setEditable(false);

        eventPublisher.registerListener(new LoggingTransactionListener());
        eventPublisher.registerListener(new SwingTransactionListener(
                logArea,
                firstAccountBalanceLabel,
                secondAccountBalanceLabel,
                firstAccount,
                secondAccount
        ));

        setTitle("Bank Transactions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel balancePanel = new JPanel(new GridLayout(2, 1));
        balancePanel.add(firstAccountBalanceLabel);
        balancePanel.add(secondAccountBalanceLabel);

        JPanel actionsPanel = new JPanel(new GridLayout(4, 3));

        JButton depositButton = new JButton("Deposit to ACC-1");
        JButton withdrawButton = new JButton("Withdraw from ACC-1");
        JButton transferButton = new JButton("Transfer ACC-1 → ACC-2");
        JButton freezeFirstButton = new JButton("Freeze ACC-1");
        JButton freezeSecondButton = new JButton("Freeze ACC-2");

        actionsPanel.add(new JLabel("Deposit amount:"));
        actionsPanel.add(depositAmountField);
        actionsPanel.add(depositButton);

        actionsPanel.add(new JLabel("Withdraw amount:"));
        actionsPanel.add(withdrawAmountField);
        actionsPanel.add(withdrawButton);

        actionsPanel.add(new JLabel("Transfer amount:"));
        actionsPanel.add(transferAmountField);
        actionsPanel.add(transferButton);

        actionsPanel.add(freezeFirstButton);
        actionsPanel.add(freezeSecondButton);
        actionsPanel.add(new JLabel());

        JScrollPane logScrollPane = new JScrollPane(logArea);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(actionsPanel, BorderLayout.NORTH);
        centerPanel.add(logScrollPane, BorderLayout.CENTER);

        add(balancePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        depositButton.addActionListener(actionEvent -> submitDeposit());
        withdrawButton.addActionListener(actionEvent -> submitWithdraw());
        transferButton.addActionListener(actionEvent -> submitTransfer());
        freezeFirstButton.addActionListener(actionEvent -> submitFreeze(firstAccount));
        freezeSecondButton.addActionListener(actionEvent -> submitFreeze(secondAccount));
    }

    private void submitDeposit() {
        String text = depositAmountField.getText();
        BigDecimal amount = parseAmountOrShowError(text);
        if (amount == null) {
            return;
        }
        TransactionRequest request = new TransactionRequest(
                TransactionType.DEPOSIT,
                userId,
                null,
                firstAccount.getAccountId(),
                amount
        );
        submitRequestAsync(request);
    }

    private void submitWithdraw() {
        String text = withdrawAmountField.getText();
        BigDecimal amount = parseAmountOrShowError(text);
        if (amount == null) {
            return;
        }
        TransactionRequest request = new TransactionRequest(
                TransactionType.WITHDRAW,
                userId,
                firstAccount.getAccountId(),
                null,
                amount
        );
        submitRequestAsync(request);
    }

    private void submitTransfer() {
        String text = transferAmountField.getText();
        BigDecimal amount = parseAmountOrShowError(text);
        if (amount == null) {
            return;
        }
        TransactionRequest request = new TransactionRequest(
                TransactionType.TRANSFER,
                userId,
                firstAccount.getAccountId(),
                secondAccount.getAccountId(),
                amount
        );
        submitRequestAsync(request);
    }

    private void submitFreeze(BankAccount account) {
        TransactionRequest request = new TransactionRequest(
                TransactionType.FREEZE,
                userId,
                null,
                account.getAccountId(),
                null
        );
        submitRequestAsync(request);
    }

    private BigDecimal parseAmountOrShowError(String text) {
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException exception) {
            SwingUtilities.invokeLater(() -> logArea.append("Invalid amount: " + text + System.lineSeparator()));
            return null;
        }
    }

    private void submitRequestAsync(TransactionRequest request) {
        Future<?> future = transactionService.submitTransaction(request);
        new Thread(() -> {
            try {
                future.get();
            } catch (Exception exception) {
                String message = "Error: " + exception.getMessage();
                SwingUtilities.invokeLater(() -> logArea.append(message + System.lineSeparator()));
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankSwingApplication application = new BankSwingApplication();
            application.setVisible(true);
        });
    }
}
