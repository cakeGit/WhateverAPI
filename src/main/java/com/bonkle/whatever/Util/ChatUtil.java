package com.bonkle.whatever.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static void Line(Player player) {
        player.sendMessage(ChCol.DARK_GRAY + "--------------------------------------------------");
    }
    public static void Line(CommandSender sender) {
        sender.sendMessage(ChCol.DARK_GRAY + "--------------------------------------------------");
    }
}
