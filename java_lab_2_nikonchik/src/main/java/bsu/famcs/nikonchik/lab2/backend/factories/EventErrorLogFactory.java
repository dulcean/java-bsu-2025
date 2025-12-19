package bsu.famcs.nikonchik.lab2.backend.factories;

import java.time.LocalDateTime;
import java.util.UUID;
import java.io.StringWriter;
import java.io.PrintWriter;

import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.lifecycleerrorlogs.FreezeErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.logs.moneyflowerrorlogs.MoneyFlowErrorLog;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.Transaction;
import org.springframework.stereotype.Component;

@Component
public class EventErrorLogFactory {
    public MoneyFlowErrorLog createMoneyFlowErrorLog(Transaction transaction, Exception e) {
        return new MoneyFlowErrorLog(UUID.randomUUID(), transaction.getEventId(),
                transaction.getInitiatorId(), LocalDateTime.now(),
                getErrorCode(e), e.getMessage(), getStackTrace(e),
                transaction.getAmount(), transaction.getClass().getSimpleName());
    }

    public FreezeErrorLog createFreezeErrorLog(AccountFreezeEvent accountFreezeEvent, Exception e) {
        return new FreezeErrorLog(UUID.randomUUID(), accountFreezeEvent.getEventId(),
                accountFreezeEvent.getInitiatorId(), LocalDateTime.now(),
                getErrorCode(e), e.getMessage(), getStackTrace(e),
                accountFreezeEvent.getActionType().toString());
    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static String getErrorCode(Exception e) {
        return e.getClass().getSimpleName();
    }
}
