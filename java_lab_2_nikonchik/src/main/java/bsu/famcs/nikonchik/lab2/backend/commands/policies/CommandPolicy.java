package bsu.famcs.nikonchik.lab2.backend.commands.policies;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.Actor;
import bsu.famcs.nikonchik.lab2.backend.commands.Command;

public interface CommandPolicy {
    boolean canExecute(Actor actor, Command command);
}
