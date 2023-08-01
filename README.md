# Dynmie's Command Framework (DCF)
A cool command framework for Minecraft.

## About
This is a stripped down version of the command framework used on [pixlies.net](https://pixlies.net/).

WARNING: This only runs on Paper. Spigot and Bukkit will not be supported.

## Contributing
Want to contribute? Create a pull request!

---

# Getting started
To get started, create a command framework:
```java
// Create the default settings for the framework
FrameworkSettings settings = new FrameworkSettings();
// Pass in your plugin instance and settings
CommandFramework framework = new CommandFramework(plugin, settings);
```
## Registering a command
Create a class that extends `BaseCommand`, then register your command with the framework.
```java
framework.register(new CoolCommand());
```

## Command Contexts
To add a context resolver for `CommandContext#argAt`, add them to the settings before passing them in to the framework.
```java
FrameworkSettings settings = new FrameworkSettings();

settings.addResolver(MyClass.class, arg -> {
        MyClass something = MyClass.getSomething(arg);
        if (something != null) {
            return something;
        }
        return null;
});

CommandFramework framework = new CommandFramework(plugin, settings);
```

## Command Completions
To add command completions, add the completions to your command constructor.
```java
public class MyCommand extends BaseCommand {
    public MyCommand() {
        setTabables(List.of(Tabables.player(), new StringTabable("wow", "cool")));
    }
}
```
To add custom command completions, implement the `Tabable` interface and add them in your constructor.
```java
public class MyTabable implements Tabable {
    @Override
    public List<String> onTab(CommandSender sender, String match) {
        List<String> ret = new ArrayList<>();
        StringUtil.copyPartialMatches(match, List.of("i", "love", "java"), ret);
        return ret;
    }
}
```
Command conditions and prerequisites use an equivalent approach. 
