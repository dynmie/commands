package me.dynmie.commands.tabable;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author dynmie
 */
public interface Tabable {

    /**
     * Ran when a sender presses tab while typing a command.
     * @param sender Sender pressing tab
     * @param match The string to match with
     * @return A list of all strings the sender may cycle through
     */
    List<String> onTab(CommandSender sender, String match);

}
