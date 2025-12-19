package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands.DepositCommand;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;

public class DepositCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof DepositCommand)) return false;

        DepositCommand cmd = (DepositCommand) command;

        if (actor.getPermissionLevel() == PermissionLevel.USER) {
            return actor.getActorId().equals(cmd.getInitiatorId());
        } else {
            return false;
        }
    }
}
