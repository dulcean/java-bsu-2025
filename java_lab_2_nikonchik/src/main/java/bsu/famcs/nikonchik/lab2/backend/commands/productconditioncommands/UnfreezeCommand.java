package bsu.famcs.nikonchik.lab2.backend.commands.productconditioncommands;

import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.policies.FreezeCommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.entities.events.lifecycleevents.AccountFreezeEvent;
import bsu.famcs.nikonchik.lab2.backend.exceptions.CommandExceptions;
import bsu.famcs.nikonchik.lab2.backend.services.AccountService;

public class UnfreezeCommand extends Command {
    private final UUID accountToUnfreeze;
    private final AccountService accountService;
    private UUID accountFreezeEventId;

    public UnfreezeCommand(UUID initiatorId, UUID accountToUnfreeze,
                         CommandStatus status, String description,
                         AccountService accountService) {
        super(initiatorId, status, description, new FreezeCommandPolicy());
        this.accountToUnfreeze = accountToUnfreeze;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            AccountFreezeEvent event = accountService.unfreezeAccount(
                    initiatorId, accountToUnfreeze, description
            );
            this.accountFreezeEventId = event.getEventId();
            this.status = CommandStatus.EXECUTED;
        } catch (Exception e) {
            this.status = CommandStatus.FAILED;
            throw new CommandExceptions.CommandExecutionException("Freeze action failed", e);
        }
    }

    public UUID getAccountFreezeEventId() { return accountFreezeEventId; }
}
