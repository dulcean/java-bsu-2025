package bank.observer;

public interface TransactionObservable {
    void addListener(TransactionObserver listener);

    void removeListener(TransactionObserver listener);
}
