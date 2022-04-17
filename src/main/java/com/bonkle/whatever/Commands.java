package com.bonkle.whatever;

import com.bonkle.whatever.CommandAPI.CommandHandler;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.MenuAPI.BigMenu;
import com.bonkle.whatever.MenuAPI.BigMenuType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

class Commands {//Internal Commands class - private to this plugin

    protected static CommandHandler handler = new CommandHandler(WhMain.plugin);

    protected static void init() {
        handler
                .registerCommandListener("customitemmenu", Commands::customItemMenu);
        Debug.log("Registered commands");
    }

    protected static boolean customItemMenu(CommandSender sender, Command cmd, String label, String[] args) {
        Debug.log("Opening Custom Item Menu");

        ArrayList<ItemStack> items = new ArrayList<>();
        for (CustomItem item : CustomItem.getCustomItems()) {
            items.add(item.generateItemStack());
        }
        Debug.log("Generated "+items.size()+" items");

        BigMenu menu = new BigMenu(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Custom Item Menu", BigMenuType.PAGE)
                .dumpContents(items);

        menu.open((Player) sender);
        return true;
    }

}
