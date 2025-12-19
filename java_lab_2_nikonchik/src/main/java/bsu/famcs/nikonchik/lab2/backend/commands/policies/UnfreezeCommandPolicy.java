package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.productconditioncommands.UnfreezeCommand;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;

public class UnfreezeCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof UnfreezeCommand)) return false;

        PermissionLevel level = actor.getPermissionLevel();

        return level == PermissionLevel.ADMIN ||
                level == PermissionLevel.CHIEF;
    }
}
