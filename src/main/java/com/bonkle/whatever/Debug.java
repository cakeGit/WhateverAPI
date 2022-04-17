package com.bonkle.whatever;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Debug {

    /*private static boolean defaultRequireOp = false;

    private static ArrayList<String> enabledDebugPackages = new ArrayList<>();
    private static ArrayList<String> enabledRequireOpPackages = new ArrayList<>();

    public static void setEnabled(boolean debugEnabled, Plugin plugin) {
        String packageName = plugin.getClass().getPackage().toString().substring(8);
        Bukkit.getLogger().info(packageName + " set debug: " + debugEnabled);

        if (debugEnabled) {
            enabledDebugPackages.add(packageName);
        } else {
            enabledDebugPackages.remove(packageName);
        }
    }

    public static void setRequireOp(boolean requireOP, Plugin plugin) {
        String packageName = plugin.getClass().getPackage().toString().substring(8);
        Bukkit.getLogger().info(packageName + " set require op: " + requireOP);

        if (requireOP) {
            enabledRequireOpPackages.add(packageName);
        } else {
            enabledRequireOpPackages.remove(packageName);
        }
    }

    public static void log(String message) { log(message, defaultRequireOp); }

    public static void log(String message, boolean requireOp) {
        //Check if stack trace contains an enabled package
        String foundPackageName = null;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String packageName = element.getClassName().substring(0, element.getClassName().lastIndexOf('.'));
            if (enabledDebugPackages.contains(packageName)) {
                foundPackageName = packageName;
                break;
            }
        }

        if (foundPackageName != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if ((requireOp && !player.isOp()) || !enabledRequireOpPackages.contains(foundPackageName)) {
                    player.sendMessage(ChatColor.DARK_GRAY + "[Debug]: " + ChatColor.RESET + message);
                }
            }
        }
    }*/

    private static ArrayList<String> enabledPackages = new ArrayList<>();

    //Add the package name of the plugin to enabled packages
    public static void enable(Plugin plugin) {
        enabledPackages.add(plugin.getClass().getPackage().toString().substring(8));
        Debug.log(enabledPackages.get(0));
    }

    private static boolean notCalledByEnabled() {
        boolean found = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String packageName = element.getClassName();
            for (String enabledPackage : enabledPackages) {
                if (packageName.contains(enabledPackage)) {
                    found = true;
                    break;
                }
            }
        }
        return !found;
    }

    private static String getPrefix() {
        return ChatColor.DARK_GRAY + "[Debug]: " + ChatColor.RESET;
    }

    public static void log(String message) {
        if (notCalledByEnabled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(getPrefix() + message);
        }
        Bukkit.getLogger().info(message + "");
    }

    public static void log(int message) {
        if (notCalledByEnabled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(getPrefix() + ChatColor.AQUA + message);
        }
        Bukkit.getLogger().info(message + "");
    }

    public static void log(boolean message) {
        if (notCalledByEnabled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(getPrefix() + (message ? ChatColor.GREEN : ChatColor.RED) + message);
        }
        Bukkit.getLogger().info(message + "");
    }

}
