package me.dynmie.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author dynmie
 */
public class RootCommand extends BukkitCommand {

    private final CommandFramework manager;

    protected RootCommand(@NotNull CommandFramework manager, @NotNull String name, @NotNull String description, @NotNull List<String> aliases) {
        super(name, description, "", aliases);
        this.manager = manager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!manager.getPlugin().isEnabled()) {
            return false;
        }

        manager.handleCommand(sender, this, label, args);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!manager.getPlugin().isEnabled()) {
            return super.tabComplete(sender, label, args);
        }

        return manager.handleTab(sender, this, label, args);
    }

}
