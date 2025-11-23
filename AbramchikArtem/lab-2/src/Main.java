import model.*;
import service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        Account a1 = new Account(UUID.randomUUID(), BigDecimal.valueOf(500));
        Account a2 = new Account(UUID.randomUUID(), BigDecimal.valueOf(100));

        User user = new User(UUID.randomUUID(), "klee", List.of(a1, a2));

        TransactionService svc = new TransactionService();

        svc.process(new Transaction(Transaction.Action.DEPOSIT,
                BigDecimal.valueOf(300),
                a1, null));

        svc.process(new Transaction(Transaction.Action.WITHDRAW,
                BigDecimal.valueOf(200),
                a1, null));

        svc.process(new Transaction(Transaction.Action.TRANSFER,
                BigDecimal.valueOf(100),
                a1, a2));

        svc.process(new Transaction(Transaction.Action.FREEZE,
                null,
                a1, null));
    }
}
