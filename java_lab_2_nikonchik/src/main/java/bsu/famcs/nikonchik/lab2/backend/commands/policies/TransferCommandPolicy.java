package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;
import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands.TransferCommand;

public class TransferCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof TransferCommand)) return false;

        TransferCommand cmd = (TransferCommand) command;

        if (actor.getPermissionLevel() == PermissionLevel.USER) {
            return actor.getActorId().equals(cmd.getInitiatorId());
        } else {
            return false;
        }
    }
}
