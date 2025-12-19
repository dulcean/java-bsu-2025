package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;
import bsu.famcs.nikonchik.lab2.backend.commands.productconditioncommands.FreezeCommand;

public class FreezeCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof FreezeCommand)) return false;

        FreezeCommand cmd = (FreezeCommand) command;
        PermissionLevel level = actor.getPermissionLevel();

        if (level == PermissionLevel.USER) {
            return actor.getActorId().equals(cmd.getInitiatorId());
        }
        return level == PermissionLevel.ADMIN ||
                level == PermissionLevel.CHIEF;
    }
}
