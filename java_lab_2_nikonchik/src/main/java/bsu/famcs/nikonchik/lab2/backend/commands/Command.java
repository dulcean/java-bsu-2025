package bsu.famcs.nikonchik.lab2.backend.commands;

import java.util.UUID;

import bsu.famcs.nikonchik.lab2.backend.commands.policies.CommandPolicy;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;

public abstract class Command {
    protected final UUID commandId;
    protected final UUID initiatorId;
    protected CommandStatus status;
    protected final String description;
    protected final CommandPolicy policy;

    protected Command(UUID initiatorId, CommandStatus status,
            String description, CommandPolicy policy) {
        this.commandId = UUID.randomUUID();
        this.initiatorId = initiatorId;
        this.status = status;
        this.description = description;
        this.policy = policy;
    }

    public boolean canExecuteBy(Actor actor) {
        return policy.canExecute(actor, this);
    }

    public enum CommandStatus {
        PENDING, EXECUTED, UNDONE, FAILED
    }

    public abstract void execute();
    public UUID getCommandId() { return commandId; }
    public UUID getInitiatorId() { return initiatorId; }
    public CommandStatus getStatus() { return status; }
}
