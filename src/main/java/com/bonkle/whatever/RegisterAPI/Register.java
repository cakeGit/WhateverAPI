package com.bonkle.whatever.RegisterAPI;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Register {

    public static void eventHandler(Plugin plugin, Listener handlerInstance) {
        plugin.getServer().getPluginManager().registerEvents(handlerInstance, plugin);
    }

}
