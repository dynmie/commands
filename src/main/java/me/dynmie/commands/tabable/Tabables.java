package me.dynmie.commands.tabable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dynmie
 */
public class Tabables {

    private static final Tabable emptyTabable = (sender, match) -> null;
    private static final Tabable worldTabable = (sender, match) -> {
        List<String> ret = new ArrayList<>();
        StringUtil.copyPartialMatches(match, Bukkit.getWorlds().stream().map(WorldInfo::getName).toList(), ret);
        Collections.sort(ret);
        return ret;
    };
    private static final Tabable playerTabable = (sender, match) -> {
        List<String> ret = new ArrayList<>();
        List<String> list = Bukkit.getServer().getOnlinePlayers().stream().filter(p -> {
            if (sender instanceof Player player) {
                return player.canSee(p);
            }
            return true;
        }).map(Player::getName).toList();

        StringUtil.copyPartialMatches(match, list, ret);
        Collections.sort(ret);

        return list;
    };

    /**
     * Gets nothing
     * @return A tabable of nothing
     */
    public static Tabable empty() {
        return emptyTabable;
    }

    /**
     * Gets a tabable of worlds
     * @return A tabable of worlds
     */
    public static Tabable world() {
        return worldTabable;
    }

    /**
     * Gets a tabable of players
     * @return A tabable of players
     */
    public static Tabable player() {
        return playerTabable;
    }

}
