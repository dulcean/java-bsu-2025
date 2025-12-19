package bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands;

import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.TransferCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.Transaction;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class TransferCommand extends MoneyFlowCommand {
    private final UUID fromAccountId;
    private final UUID toAccountId;

    public TransferCommand(UUID initiatorId, UUID fromAccountId,
                           UUID toAccountId, BigDecimal amount,
                           String description, AccountService accountService) {
        super(initiatorId, CommandStatus.PENDING, description,
                new TransferCommandPolicy(), amount, accountService);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    @Override
    public void execute() {
        try {
            Transaction transaction = accountService.transferFunds(
                    initiatorId, fromAccountId, toAccountId, amount, description
            );
            this.transactionId = transaction.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Transfer failed", e);
        }
    }

    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
}
