package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.commands.Command;
import bsu.famcs.nikonchik.lab2.backend.commands.moneyflowcommands.WithdrawalCommand;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.entities.actors.PermissionLevel;

public class WithdrawalCommandPolicy implements CommandPolicy {
    @Override
    public boolean canExecute(Actor actor, Command command) {
        if (!(command instanceof WithdrawalCommand)) return false;

        WithdrawalCommand cmd = (WithdrawalCommand) command;

        if (actor.getPermissionLevel() == PermissionLevel.USER) {
            return actor.getActorId().equals(cmd.getInitiatorId());
        } else {
            return false;
        }

        //Other business validation may be added (withdrawal limitations and so on)
    }
}
