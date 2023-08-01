package me.dynmie.commands.tabable.impl;

import lombok.Getter;
import me.dynmie.commands.tabable.Tabable;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cycle through strings
 * @author dynmie
 */
public class StringTabable implements Tabable {

    private final @Getter List<String> strings;

    public StringTabable(String... strings) {
        this.strings = Arrays.asList(strings);
    }

    public StringTabable(List<String> strings) {
        this.strings = strings;
    }

    @Override
    public List<String> onTab(CommandSender sender, String match) {

        List<String> ret = new ArrayList<>();
        StringUtil.copyPartialMatches(match, strings, ret);

        return ret;
    }

}
