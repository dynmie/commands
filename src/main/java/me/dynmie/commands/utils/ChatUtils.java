package me.dynmie.commands.utils;

import org.bukkit.ChatColor;

/**
 * @author dynmie
 */
public class ChatUtils {

    @SuppressWarnings("deprecation") // legacy chat formatting
    public static String format(String fc) {
        return ChatColor.translateAlternateColorCodes('&', fc);
    }

}
