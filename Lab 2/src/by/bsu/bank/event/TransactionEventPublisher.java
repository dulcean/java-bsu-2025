package by.bsu.bank.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionEventPublisher {
    private final List<TransactionListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(TransactionListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(TransactionListener listener) {
        listeners.remove(listener);
    }

    public void publishEvent(TransactionEvent event) {
        for (TransactionListener listener : listeners) {
            listener.handle(event);
        }
    }
}
