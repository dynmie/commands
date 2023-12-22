package me.dynmie.commands.condition;

import me.dynmie.commands.CommanResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author dynmie
 */
public class Conditions {

    private static final CommandCondition emptyCondition = (sender, arg) -> CommanResult.OK;

    private static final CommandCondition playerCondition = (sender, arg) -> {
        Player player = Bukkit.getPlayer(arg);

        if (player == null) {
            try {
                player = Bukkit.getPlayer(UUID.fromString(arg));
            } catch (IllegalArgumentException ignored) {

            }
        }

        if (player == null) {
            return CommanResult.PLAYER_NOT_EXIST;
        }

        return CommanResult.OK;
    };

    private static final CommandCondition worldCondition = (sender, arg) -> {
        if (Bukkit.getWorld(arg) == null) {
            return CommanResult.INCORRECT_USAGE;
        }
        return CommanResult.OK;
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
