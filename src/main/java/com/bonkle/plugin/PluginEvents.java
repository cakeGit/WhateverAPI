package com.bonkle.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;

public class PluginEvents implements Listener {

    public static HashMap<Player, ArmorStand> cameraOperators = new HashMap<>();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (cameraOperators.containsKey(event.getPlayer())) {

            Location location = cameraOperators.get(event.getPlayer()).getLocation();

            event.getPlayer().getLocation().set(
                    location.getX(),
                    location.getY(),
                    location.getZ()
            );

            cameraOperators.get(event.getPlayer()).setHeadPose(
                    new EulerAngle(
                            Math.toRadians(event.getPlayer().getLocation().getPitch()),
                            Math.toRadians(event.getPlayer().getLocation().getYaw()),
                            0));
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (cameraOperators.containsKey(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.AQUA + "You are no longer a camera operator.");
            cameraOperators.remove(event.getPlayer());
        }
    }
}
