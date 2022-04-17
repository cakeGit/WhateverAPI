package com.bonkle.whatever.CommandAPI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * <h2>Interface for command listeners</h2>
 *
 * Methods / Lambdas will be passed all the arguments from the command:
 * <ul>
 *     <li>{@code CommandSender sender} - The sender of the command</li>
 *     <li>{@code Command command} - The command that was executed</li>
 *     <li>{@code String label} - The command label</li>
 *     <li>{@code String[] args} - The arguments passed to the command</li>
 * </ul>
 *
 * <hr><br>
 *
 * Example usages:
 * <blockquote>
 * You can either use a lambda or a method reference,<br><br>
 *
 * Lambda Example:<br>
 *
 * <code><pre>
 * //Argument names can be changed, however,
 * //The order of the arguments cannot.
 * CommandFunction cmdFunc = (sender, command, label, args) -> {
 *    sender.sendMessage("Hello World!");
 *    //Return true to indicate that the command
 *    //was handled successfully
 *    return true;
 * };</pre></code>
 *
 * Method Reference Example:<br>
 *
 * <code><pre>
 * //Will pass exampleCommandHandler() in
 * //YourHandlerClass as the CommandFunction
 * CommandFunction cmdFunc2 =
 *    YourCommandHandlerClass::exampleCommandHandler
 * ;
 * </pre></code>
 *
 * </blockquote>
 *
 * <hr><br><img src="https://i.ibb.co/724pJ0Y/JD-128x128.png" height="60"><br><span color="gray">Whatever API - Javadocs By Cak - com.bonkle.whatever</span><br>
 * @see CommandHandler
 * @see CommandFunction#onCommand(CommandSender sender, Command command, String label, String[] args)
 */
public interface CommandFunction {
    /**
     * <h2>Command Function - Used to handle commands registered via <a href="CommandHandler">CommandHandler</a></h2>
     * Methods / Lambdas will be passed all the arguments from the command:
     * <ul>
     *     <li>{@code CommandSender sender} - The sender of the command</li>
     *     <li>{@code Command command} - The command that was executed</li>
     *     <li>{@code String label} - The command label</li>
     *     <li>{@code String[] args} - The arguments passed to the command</li>
     * </ul>
     * <hr><br>
     *
     * Example usages:
     * <blockquote>
     * You can either use a lambda or a method reference,<br><br>
     *
     * Lambda Example:<br>
     *
     * <code><pre>
     * //Argument names can be changed, however,
     * //The order of the arguments cannot.
     * CommandFunction cmdFunc = (sender, command, label, args) -> {
     *    sender.sendMessage("Hello World!");
     *    //Return true to indicate that the command
     *    //was handled successfully
     *    return true;
     * };</pre></code>
     *
     * Method Reference Example:<br>
     *
     * <code><pre>
     * //Will pass exampleCommandHandler() in
     * //YourHandlerClass as the CommandFunction
     * CommandFunction cmdFunc2 =
     *    YourCommandHandlerClass::exampleCommandHandler
     * ;
     * </pre></code>
     *
     * </blockquote>
     *
     * <hr><br><img src="https://i.ibb.co/724pJ0Y/JD-128x128.png" height="60"><br><span color="gray">Whatever API - Javadocs By Cak - com.bonkle.whatever</span><br>
     * @param sender The sender of the command
     * @param command The command being executed
     * @param label The label of the command
     * @param args The arguments of the command
     * @return True if the command was handled, false if not
     */
    boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}
