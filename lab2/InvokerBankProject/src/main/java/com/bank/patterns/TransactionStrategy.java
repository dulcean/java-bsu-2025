package com.bank.patterns;

import com.bank.model.Account;
import com.bank.model.Transaction;
import java.util.Map;
import java.util.UUID;

public interface TransactionStrategy {
    void execute(Account account, Transaction tx, Map<UUID, Account> allAccounts);
}
