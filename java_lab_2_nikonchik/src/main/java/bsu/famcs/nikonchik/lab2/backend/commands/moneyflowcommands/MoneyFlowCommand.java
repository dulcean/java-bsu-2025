package bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands;

import java.math.BigDecimal;
import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.policies.CommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;

public abstract class MoneyFlowCommand extends Command {
    protected final AccountService accountService;
    protected final BigDecimal amount;
    protected UUID transactionId;

    protected MoneyFlowCommand(UUID initiatorId, CommandStatus status,
                               String description, CommandPolicy policy,
                               BigDecimal amount, AccountService accountService) {
        super(initiatorId, status, description, policy);
        this.amount = amount;
        this.accountService = accountService;
    }

    public UUID getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
}
