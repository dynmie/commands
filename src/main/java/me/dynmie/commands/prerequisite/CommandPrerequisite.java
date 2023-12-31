package me.dynmie.commands.prerequisite;

import me.dynmie.commands.CommandResult;
import org.bukkit.command.CommandSender;

/**
 * @author dynmie
 */
public interface CommandPrerequisite {

    /**
     * Ran before a command is run.
     * @param sender Sender executing the command
     * @return The status
     */
    CommandResult check(CommandSender sender);

}
