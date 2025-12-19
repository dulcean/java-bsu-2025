package bsu.famcs.nikonchik.lab2.backend.commands.productconditioncommands;

import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.policies.FreezeCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions.CommandExecutionException;

public class FreezeCommand extends Command {
    private final UUID accountToFreeze;
    private final AccountService accountService;
    private UUID accountFreezeEventId;

    public FreezeCommand(UUID initiatorId, UUID accountToFreeze,
                         CommandStatus status, String description,
                         AccountService accountService) {
        super(initiatorId, status, description, new FreezeCommandPolicy());
        this.accountToFreeze = accountToFreeze;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            AccountFreezeEvent event = accountService.freezeAccount(
                    initiatorId, accountToFreeze, description
            );
            this.accountFreezeEventId = event.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExecutionException("Freeze action failed", e);
        }
    }

    public UUID getAccountFreezeEventId() { return accountFreezeEventId; }
}
