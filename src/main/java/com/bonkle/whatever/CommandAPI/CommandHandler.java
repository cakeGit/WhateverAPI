package com.bonkle.whatever.CommandAPI;

import com.bonkle.whatever.Debug;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * <h2>Api wrapper to make handling bukkit commands easier</h2>
 *
 * <b color="#FFED20">Note: Still requires the command to be registered in the plugin.yml</b><br><br>
 *
 * Example usage:<br>
 * <code><pre>   CommandHandler handler = new CommandHandler(YourPlugin)
 *    .registerCommandListener("test",
 *       (sender, command, label, args) -> {
 *          sender.sendMessage("Hello World!");
 *          //Return true to indicate that the command
 *          //was handled successfully
 *          return true;
 *       }
 *    )
 *    .registerCommandListener("test2",
 *       YourCommandHandlerClass::test2Command
 *    ); //You can also set it to run another function</pre></code>
 *
 *
 * @see CommandHandler#CommandHandler(JavaPlugin plugin)
 */
public class CommandHandler implements CommandExecutor {

    private final JavaPlugin plugin;
    private HashMap<String, CommandFunction> listeners = new HashMap<>();

    /**
     * <h2>Api wrapper to make handling bukkit commands easier</h2>
     * <b color="#FFED20">Note: Still requires the command to be registered in the plugin.yml</b><br><br>
     * Add listeners using the
     * <a href="CommandHandler#registerCommandListener"><code>registerCommandListener(String commandName, CommandFunction onCommand)</code></a>
     * method<br><br>
     *
     * Example usage:<br>
     * <code><pre>   CommandHandler handler = new CommandHandler(YourPlugin)
     *    .registerCommandListener("test",
     *       (sender, command, label, args) -> {
     *          sender.sendMessage("Hello World!");
     *          //Return true to indicate that the command
     *          //was handled successfully
     *          return true;
     *       }
     *    )
     *    .registerCommandListener("test2",
     *       YourCommandHandlerClass::test2Command
     *    ); //You can also set it to run another function</pre></code>
     *
     * @param plugin The plugin that is registering the command
     * @see CommandHandler#registerCommandListener(String commandName, CommandFunction function)
     * @see CommandFunction#onCommand(CommandSender sender, Command command, String label, String[] args)
     */
    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * <h2>Function to register a command listener to the CommandHandler</h2>
     * <b color="#FFED20">Note: The command must be registered in the plugin.yml</b><br><br>
     *
     * Example usage:<br>
     * <code><pre>   CommandHandler handler = new CommandHandler(YourPlugin)
     *    .registerCommandListener("test",
     *       (sender, command, label, args) -> {
     *          sender.sendMessage("Hello World!");
     *          //Return true to indicate that the command
     *          //was handled successfully
     *          return true;
     *       }
     *    )
     *    .registerCommandListener("test2",
     *       YourCommandHandlerClass::test2Command
     *    ); //You can also set it to run another function</pre></code>
     *
     * @param command The command to register
     * @param function The CommandFunction to be executed when the command is ran
     */
    public CommandHandler registerCommandListener(String command, CommandFunction function) {
        Objects.requireNonNull(plugin.getCommand(command)).setExecutor(this);
        listeners.put(command, function);
        Debug.log("Registered listener for command: " + command);
        return this;
    }

    /**
     * <h2>Internal function to handle command execution</h2>
     * This function is called by Bukkit when a command is executed<br><br>
     * <b color="#FF2020">This function is not meant to be called by the plugin using this API and as such is deprecated</b><br><br>
     *
     * @param sender The sender of the command
     * @param command The command that was executed
     * @param label The command label
     * @param args Arguments passed to the command
     * @return true if the command was handled, false if not
     */
    @Override
    @Deprecated
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (listeners.containsKey(command.getName())) {
            CommandFunction function = listeners.get(command.getName());
            return function.onCommand(sender, command, label, args);
        }
        return false;
    }
}
