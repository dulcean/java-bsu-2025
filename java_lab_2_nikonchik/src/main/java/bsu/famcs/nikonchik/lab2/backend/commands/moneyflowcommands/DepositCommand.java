package bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands;

import java.util.UUID;
import java.math.BigDecimal;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.DepositCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.DepositTransaction;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class DepositCommand extends MoneyFlowCommand {
    private final UUID toAccountId;
    private final String depositMethod;

    public DepositCommand(UUID initiatorId, UUID toAccountId,
                          BigDecimal amount, String description,
                          String depositMethod, AccountService accountService) {
        super(initiatorId, CommandStatus.PENDING, description,
                new DepositCommandPolicy(), amount, accountService);
        this.toAccountId = toAccountId;
        this.depositMethod = depositMethod;
    }

    @Override
    public void execute() {
        try {
            DepositTransaction transaction = accountService.depositFunds(
                    initiatorId, toAccountId, amount, description, depositMethod
            );
            this.transactionId = transaction.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Deposit failed", e);
        }
    }

    public UUID getToAccountId() { return toAccountId; }
    public String getDepositMethod() { return depositMethod; }
}
