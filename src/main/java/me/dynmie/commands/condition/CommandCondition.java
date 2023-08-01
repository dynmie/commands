package me.dynmie.commands.condition;

import me.dynmie.commands.CommandStatus;
import org.bukkit.command.CommandSender;

/**
 * @author dynmie
 */
public interface CommandCondition {

    /**
     * Forces an argument to have a certain condition before executing the command.
     * @param sender Sender executing the command
     * @param arg Argument to check for
     * @return Status of the check
     */
    CommandStatus check(CommandSender sender, String arg);

}
