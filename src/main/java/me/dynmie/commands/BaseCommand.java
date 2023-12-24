package me.dynmie.commands;

import lombok.Getter;
import lombok.Setter;
import me.dynmie.commands.condition.CommandCondition;
import me.dynmie.commands.prerequisite.CommandPrerequisite;
import me.dynmie.commands.tabable.Tabable;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author dynmie
 */
public abstract class BaseCommand {

    private final @Getter List<String> aliases;
    private @Setter @Getter boolean playerOnly = false;
    private @Setter @Getter boolean async = false;
    private @Setter @Getter String description = "A cool command!";
    private @Setter @Getter String permission = null;
    private @Setter @Getter String usage = "";
    private @Setter @Getter int minArgs = -1;
    private @Setter @Getter int maxArgs = -1;
    private @Setter @Getter List<CommandCondition> conditions = null;
    private @Setter @Getter List<Tabable> tabables = null;
    private @Setter @Getter boolean hidden;
    private @Setter @Getter String displayName;
    private @Setter @Getter List<CommandPrerequisite> prerequisites = null;

    private final @Getter String name;

    private final @Getter Map<String, BaseCommand> subcommandNames = new HashMap<>();
    private final @Getter Map<String, BaseCommand> subcommandAliases = new HashMap<>();

    public BaseCommand(List<String> aliases) {
        this.aliases = aliases;
        name = aliases.get(0);
        displayName = name;
    }

    public Map<String, BaseCommand> getSubcommands() {
        Map<String, BaseCommand> ret = new HashMap<>(subcommandAliases);
        ret.putAll(subcommandNames);

        return ret;
    }

    public void addSubcommand(@NotNull BaseCommand command) {
        subcommandNames.put(command.getName(), command);

        for (String alias : command.getAliases()) {
            if (alias.equals(command.getName())) {
                continue;
            }
            subcommandAliases.put(alias, command);
        }
    }

    public void addSubcommands(@NotNull BaseCommand... commands) {
        for (BaseCommand command : commands) {
            addSubcommand(command);
        }
    }

    public abstract @NotNull CommandResult onExecute(@NotNull CommandContext context);

    public @Nullable List<String> onTab(@NotNull CommandContext context) {
        int size = context.getArgs().size();
        if (size == 0 || size == 1) {
            List<String> ret = new ArrayList<>();

            StringUtil.copyPartialMatches(size == 0 ? "" : context.getArgAt(0), this.getSubcommandNames().entrySet().stream()
                    .filter(e -> !e.getValue().isHidden())
                    .map(Map.Entry::getKey)
                    .toList(), ret);

            if (tabables != null && !tabables.isEmpty() && tabables.get(0) != null) {
                Tabable tabable = tabables.get(0);

                List<String> tab = new ArrayList<>();
                List<String> originals = tabable.onTab(context.getSender(), "");

                if (originals != null) {
                    StringUtil.copyPartialMatches(size == 0 ? "" : context.getArgAt(0), originals, tab);
                }

                ret.addAll(tab);
            }

            Collections.sort(ret);
            return ret;
        }

        int index = size - 1;
        String arg = context.getArgs().get(index);

        if (tabables == null) return null;
        if (tabables.isEmpty()) return null;
        if (index >= tabables.size()) return null;
        Tabable tabable = tabables.get(index);

        if (tabable == null) return null;

        return tabable.onTab(context.getSender(), arg);
    }

}
