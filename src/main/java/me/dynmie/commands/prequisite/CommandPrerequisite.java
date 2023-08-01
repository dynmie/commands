package me.dynmie.commands.prequisite;

import me.dynmie.commands.CommandStatus;
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
    CommandStatus check(CommandSender sender);

}
