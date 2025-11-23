package by.bsu.bank.strategy;

import by.bsu.bank.exception.AccountNotFoundException;
import by.bsu.bank.model.BankAccount;
import by.bsu.bank.model.transaction.BaseTransaction;
import by.bsu.bank.model.transaction.WithdrawTransaction;
import by.bsu.bank.repository.AccountRepository;

import java.util.UUID;

public class WithdrawTransactionStrategy implements TransactionStrategy {
    private final AccountRepository accountRepository;

    public WithdrawTransactionStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(BaseTransaction transaction) {
        if (!(transaction instanceof WithdrawTransaction)) {
            throw new IllegalArgumentException("WithdrawTransactionStrategy supports only WithdrawTransaction");
        }
        WithdrawTransaction withdrawTransaction = (WithdrawTransaction) transaction;
        UUID accountId = withdrawTransaction.getSourceAccountId();
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found " + accountId));
        account.withdraw(withdrawTransaction.getAmount());
        accountRepository.save(account, withdrawTransaction.getUserId());
    }
}
