package me.dynmie.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author dynmie
 */
public class CommandContext {

    private final @Getter BaseCommand command;
    private final @Getter String path;
    private final @Getter CommandFramework framework;
    private final @Getter CommandSender sender;
    private final @Getter String label;
    private final @Getter List<String> args;

    public CommandContext(BaseCommand command, String path, CommandFramework framework, CommandSender sender, String label, List<String> args) {
        this.command = command;
        this.path = path;
        this.framework = framework;
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        return (Player) sender;
    }

    public int size() {
        return args.size();
    }

    public boolean isPlayerAt(int i) {
        return getPlayerAt(i) != null;
    }

    public boolean hasPermission(String node) {
        return sender.hasPermission(node);
    }

    public Player getPlayerAt(int i) {
        if (!isArgAt(i)) {
            return null;
        }

        String s = args.get(i);

        Player player = Bukkit.getPlayer(s);

        if (player == null) {
            try {
                player = Bukkit.getPlayer(UUID.fromString(s));
            } catch (IllegalArgumentException ignored) {

            }
        }

        return player;
    }

    public String getArgAt(int i) {
        if (!isArgAt(i)) {
            return null;
        }

        return args.get(i);
    }

    public boolean isArgAt(int i) {
        return !(i >= args.size());
    }

    public World getWorldAt(int i) {
        if (!isArgAt(i)) {
            return null;
        }
        return Bukkit.getWorld(getArgAt(i));
    }

    public boolean isWorldAt(int i) {
        return getWorldAt(i) != null;
    }

    public String getStringAll() {
        StringJoiner joiner = new StringJoiner(" ");

        for (String arg : args) {
            joiner.add(arg);
        }

        return joiner.toString();
    }

    public String getStringAt(int pos) {
        if (!isArgAt(pos)) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (int i = 0; i < args.size(); i++) {
            if (pos > i) continue;

            String s = args.get(i);
            joiner.add(s);
        }

        return joiner.toString();
    }

    public Integer getIntegerAt(int pos) {
        if (!isArgAt(pos)) {
            return null;
        }

        return Integer.parseInt(getArgAt(pos));
    }

    public boolean isIntegerAt(int pos) {
        return getIntegerAt(pos) != null;
    }

    public <T> T getAt(int pos, Class<T> clazz) {
        if (!isArgAt(pos)) {
            return null;
        }

        var function = framework.getSettings().getResolvers().get(clazz);
        if (function == null) {
            return null;
        }

        T ret;
        try {
            //noinspection unchecked
            ret = (T) function.apply(getArgAt(pos));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }

        return ret;
    }

    public <T> boolean isAt(int pos, Class<T> clazz) {
        return getAt(pos, clazz) != null;
    }

}
