package bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands;

import java.math.BigDecimal;
import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.commands.policies.WithdrawalCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.entities.events.moneyflowevents.WithdrawalTransaction;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class WithdrawalCommand extends MoneyFlowCommand {
    private final UUID fromAccountId;
    private final String withdrawalLocation;

    public WithdrawalCommand(UUID initiatorId, UUID fromAccountId,
                             BigDecimal amount, String description,
                             String withdrawalLocation, AccountService accountService) {
        super(initiatorId, CommandStatus.PENDING, description,
                new WithdrawalCommandPolicy(), amount, accountService);
        this.fromAccountId = fromAccountId;
        this.withdrawalLocation = withdrawalLocation;
    }

    @Override
    public void execute() {
        try {
            WithdrawalTransaction transaction = accountService.withdrawFunds(
                    initiatorId, fromAccountId, amount, description, withdrawalLocation
            );
            this.transactionId = transaction.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Withdrawal failed", e);
        }
    }

    public UUID getFromAccountId() { return fromAccountId; }
    public String getWithdrawalLocation() { return withdrawalLocation; }
}