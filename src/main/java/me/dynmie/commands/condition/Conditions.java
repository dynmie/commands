package me.dynmie.commands.condition;

import me.dynmie.commands.CommandResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author dynmie
 */
public class Conditions {

    private static final CommandCondition emptyCondition = (sender, arg) -> CommandResult.OK;

    private static final CommandCondition playerCondition = (sender, arg) -> {
        Player player = Bukkit.getPlayer(arg);

        if (player == null) {
            try {
                player = Bukkit.getPlayer(UUID.fromString(arg));
            } catch (IllegalArgumentException ignored) {

            }
        }

        if (player == null) {
            return CommandResult.PLAYER_NOT_EXIST;
        }

        return CommandResult.OK;
    };

    private static final CommandCondition worldCondition = (sender, arg) -> {
        if (Bukkit.getWorld(arg) == null) {
            return CommandResult.INCORRECT_USAGE;
        }
        return CommandResult.OK;
    };

    public static CommandCondition empty() {
        return emptyCondition;
    }

    public static CommandCondition player() {
        return playerCondition;
    }

    public static CommandCondition world() {
        return worldCondition;
    }

}
