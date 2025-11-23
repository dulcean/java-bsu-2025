import com.bank.model.Account;
import com.bank.model.User;
import com.bank.repository.BankRepository;
import com.bank.service.BankSystem;
import com.bank.ui.BankDashboard;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BankSystem bank = BankSystem.getInstance();

        User user1 = new User("GenshinFan1");
        User user2 = new User("ZhongliMain");

        Account acc1 = new Account(new BigDecimal("1000"));
        Account acc2 = new Account(new BigDecimal("500"));
        Account acc3 = new Account(new BigDecimal("0"));

        user1.addAccount(acc1);
        user1.addAccount(acc3);
        user2.addAccount(acc2);

        BankRepository.getInstance().saveAccount(acc1);
        BankRepository.getInstance().saveAccount(acc2);
        BankRepository.getInstance().saveAccount(acc3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        SwingUtilities.invokeLater(() -> {
            BankDashboard dashboard = new BankDashboard(users);
            dashboard.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(bank::shutdown));
    }
}