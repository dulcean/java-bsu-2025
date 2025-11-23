package bank.async;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LocksManager {

    private final Map<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void lock(UUID accountId) {
        ReentrantLock lock = locks.computeIfAbsent(accountId, k -> new ReentrantLock());
        lock.lock();
    }

    public void unlock(UUID accountId) {
        ReentrantLock lock = locks.get(accountId);
        if (lock != null) {
            lock.unlock();
        }
    }

    public void lockTwo(UUID first, UUID second) {
        if (first.compareTo(second) < 0) {
            lock(first);
            lock(second);
        } else {
            lock(second);
            lock(first);
        }
    }

    public void unlockTwo(UUID first, UUID second) {
        if (first.compareTo(second) < 0) {
            unlock(second);
            unlock(first);
        } else {
            unlock(first);
            unlock(second);
        }
    }
}
