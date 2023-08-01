package me.dynmie.commands.condition;

import me.dynmie.commands.CommandStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author dynmie
 */
public class Conditions {

    private static final CommandCondition emptyCondition = (sender, arg) -> CommandStatus.OK;

    private static final CommandCondition playerCondition = (sender, arg) -> {
        Player player = Bukkit.getPlayer(arg);

        if (player == null) {
            try {
                player = Bukkit.getPlayer(UUID.fromString(arg));
            } catch (IllegalArgumentException ignored) {

            }
        }

        if (player == null) {
            return CommandStatus.PLAYER_NOT_EXIST;
        }

        return CommandStatus.OK;
    };

    private static final CommandCondition worldCondition = (sender, arg) -> {
        if (Bukkit.getWorld(arg) == null) {
            return CommandStatus.INCORRECT_USAGE;
        }
        return CommandStatus.OK;
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
