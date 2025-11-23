package by.bsu.bank.strategy;

import by.bsu.bank.exception.AccountNotFoundException;
import by.bsu.bank.model.BankAccount;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.FreezeTransaction;
import by.bsu.bank.repository.AccountRepository;

import java.util.UUID;

public class FreezeTransactionStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public FreezeTransactionStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(BaseTransaction transaction) {
        if (!(transaction instanceof FreezeTransaction)) {
            throw new IllegalArgumentException("FreezeTransactionStrategy supports only FreezeTransaction");
        }
        FreezeTransaction freezeTransaction = (FreezeTransaction) transaction;
        UUID accountId = freezeTransaction.getTargetAccountId();
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found " + accountId));
        account.freeze();
        accountRepository.save(account, freezeTransaction.getUserId());
    }
}
