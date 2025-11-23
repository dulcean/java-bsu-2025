package by.bsu.bank.ui;

import by.bsu.bank.event.TransactionEvent;
import by.bsu.bank.event.TransactionListener;
import by.bsu.bank.model.BankAccount;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SwingTransactionListener implements TransactionListener {
    private final JTextArea logArea;
    private final JLabel firstAccountBalanceLabel;
    private final JLabel secondAccountBalanceLabel;
    private final BankAccount firstAccount;
    private final BankAccount secondAccount;

    public SwingTransactionListener(JTextArea logArea,
                                    JLabel firstAccountBalanceLabel,
                                    JLabel secondAccountBalanceLabel,
                                    BankAccount firstAccount,
                                    BankAccount secondAccount) {
        this.logArea = logArea;
        this.firstAccountBalanceLabel = firstAccountBalanceLabel;
        this.secondAccountBalanceLabel = secondAccountBalanceLabel;
        this.firstAccount = firstAccount;
        this.secondAccount = secondAccount;
    }

    @Override
    public void handle(TransactionEvent event) {
        String text = "Event: " + event.getType()
                + ", transactionId=" + event.getTransaction().getTransactionId()
                + ", message=" + event.getMessage();
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + System.lineSeparator());
            firstAccountBalanceLabel.setText("ACC-1 balance: " + firstAccount.getBalance());
            secondAccountBalanceLabel.setText("ACC-2 balance: " + secondAccount.getBalance());
        });
    }
}
