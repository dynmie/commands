package me.dynmie.commands;

import lombok.Getter;
import lombok.Setter;
import me.dynmie.commands.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author dynmie
 */
public class FrameworkSettings {

    private @Getter @Setter Consumer<CommandSender> onNoPerm = s -> s.sendMessage(ChatUtils.format("No permissions."));
    private @Getter @Setter Consumer<CommandSender> onNotPlayer = s -> s.sendMessage(ChatUtils.format("You cannot execute this command as console."));
    private @Getter @Setter Consumer<CommandSender> onPlayerNotExist = s -> s.sendMessage(ChatUtils.format("That player doesn't exist."));
    private @Getter @Setter BiConsumer<CommandSender, String> onIncorrectUsage = (sender, usage) -> sender.sendMessage(ChatUtils.format("&cUsage: /") + usage);
    private @Getter @Setter Consumer<CommandSender> onNotFound = s ->
            s.sendMessage(ChatUtils.format("&cThis command was not handled properly or was disabled by the server admin."));

    private final @Getter Map<Class<?>, Function<String, ?>> resolvers = new HashMap<>();

    public <T> void addResolver(Class<T> clazz, Function<String, T> resolver) {
        resolvers.put(clazz, resolver);
    }

}
