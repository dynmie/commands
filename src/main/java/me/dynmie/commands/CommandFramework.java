package me.dynmie.commands;

import lombok.Getter;
import me.dynmie.commands.condition.CommandCondition;
import me.dynmie.commands.prequisite.CommandPrerequisite;
import me.dynmie.commands.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dynmie
 */
public class CommandFramework {

    private final Map<String, BaseCommand> registeredCommands = new HashMap<>();

    private final @Getter FrameworkSettings settings;

    private final @Getter JavaPlugin plugin;

    public CommandFramework(JavaPlugin plugin, FrameworkSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public void register(BaseCommand command) {
        Bukkit.getCommandMap().register(plugin.getName(), new RootCommand(
                this,
                command.getName(),
                command.getDescription(),
                command.getAliases().stream().filter(s -> !s.equals(command.getName())).collect(Collectors.toList())
        ) {{
            setPermission(command.getPermission());
        }});
        registeredCommands.put(command.getName(), command);
    }

    public void handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
        BaseCommand baseCommand = registeredCommands.get(cmd.getName());
        if (baseCommand == null) {
            settings.getOnNotFound().accept(sender);
            return;
        }



        StringJoiner path = new StringJoiner(" ");
        path.add(baseCommand.getName());

        BaseCommand previous;
        BaseCommand current = baseCommand;
        int pos = 0;
        boolean subcommand = false;
        for (String arg : args) {
            previous = current;

            if (previous.getPermission() != null) {
                if (!sender.hasPermission(previous.getPermission())) {
                    settings.getOnNoPerm().accept(sender);
                    return;
                }
            }

            current = previous.getSubcommands().get(arg);

            if (current == null) {
                current = previous;
                break;
            }

            path.add(current.getName());
            subcommand = true;
            pos++;
        }

        // perm check again for current cmd
        if (current.getPermission() != null) {
            if (!sender.hasPermission(current.getPermission())) {
                settings.getOnNoPerm().accept(sender);
                return;
            }
        }

        String usage = path + " " + current.getUsage();
        String pathStr = path.toString();


        if (!(sender instanceof Player) && current.isPlayerOnly()) {
            settings.getOnNotPlayer().accept(sender);
            return;
        }



        List<String> argsToReturn = new ArrayList<>(Arrays.asList(args));
        if (subcommand) {
            argsToReturn = new ArrayList<>(argsToReturn.subList(pos, args.length));
        }

        CommandContext context = new CommandContext(
                current,
                pathStr,
                this,
                sender,
                label,
                argsToReturn
        );



        // MIN MAX ARGS
        if (current.getMinArgs() != -1 && argsToReturn.size() < current.getMinArgs()) {
            settings.getOnIncorrectUsage().accept(sender, usage);
            return;
        }

        if (current.getMaxArgs() != -1 && argsToReturn.size() > current.getMaxArgs()) {
            settings.getOnIncorrectUsage().accept(sender, usage);
            return;
        }

        // CONDITION
        if (current.getPrerequisites() != null) {
            for (CommandPrerequisite prerequisite : current.getPrerequisites()) {
                CommandStatus status = prerequisite.check(sender);
                runStat(sender, status, usage);

                if (!status.isSuccess()) return;
            }
        }

        if (current.getConditions() != null) {
            for (int i = 0; i < argsToReturn.size(); i++) {
                if (current.getConditions().isEmpty()) continue;
                if (i >= current.getConditions().size()) continue;

                CommandCondition condition = current.getConditions().get(i);
                if (condition == null) continue;

                CommandStatus status = condition.check(sender, argsToReturn.get(i));
                runStat(sender, status, usage);

                if (!status.isSuccess()) return;
            }
        }


        // EXECUTION
        BaseCommand cmdToRun = current;
        if (cmdToRun.isAsync()) {
            Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> execute(cmdToRun, context, usage));
            return;
        }

        execute(cmdToRun, context, usage);

    }

    private void execute(BaseCommand command, CommandContext context, String usage) {
        CommandSender sender = context.getSender();

        CommandStatus status;
        try {
            status = command.onExecute(context);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            sender.sendMessage(ChatUtils.format("&cAn error occurred while executing this command."));
            return;
        }

        runStat(sender, status, usage);
    }

    private void runStat(CommandSender sender, CommandStatus status, String usage) {
        switch (status) {
            case INCORRECT_USAGE -> settings.getOnIncorrectUsage().accept(sender, usage);
            case NO_PERMISSIONS -> settings.getOnNoPerm().accept(sender);
            case PLAYER_NOT_EXIST -> settings.getOnPlayerNotExist().accept(sender);
        }
    }

    public List<String> handleTab(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        BaseCommand baseCommand = registeredCommands.get(cmd.getName());
        if (baseCommand == null) {
            return null;
        }


        StringJoiner pathJoiner = new StringJoiner(" ");
        pathJoiner.add(baseCommand.getName());
        BaseCommand prevCommand = baseCommand;
        BaseCommand command = baseCommand;
        BaseCommand next;
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            index = i;

            next = command.getSubcommands().get(arg);
            if (next == null) {
                break;
            }

            pathJoiner.add(command.getName());
            prevCommand = command;
            command = next;
        }


        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            return null;
        }

        if (args[index].equals(command.getName())) {
            return tab(sender, prevCommand, index, pathJoiner.toString(), label, args);
        }


        return tab(sender, command, index, pathJoiner.toString(), label, args);
    }

    private List<String> tab(@NotNull CommandSender sender, @NotNull BaseCommand command, int index, String path, @NotNull String label, @NotNull String[] args) {

        List<String> splitArgs = new ArrayList<>(Arrays.asList(args).subList(index, args.length));
        CommandContext context = new CommandContext(
                command,
                path,
                this,
                sender,
                label,
                splitArgs
        );

        return command.onTab(context);
    }


}
