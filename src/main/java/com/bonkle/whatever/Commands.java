package com.bonkle.whatever;

import com.bonkle.whatever.CommandAPI.CommandHandler;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.MenuAPI.BigMenu;
import com.bonkle.whatever.MenuAPI.BigMenuType;
import com.bonkle.whatever.MenuAPI.MenuClickHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

class Commands {//Internal Commands class - private to this plugin

    protected static CommandHandler handler = new CommandHandler(WhMain.getPlugin());

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

        MenuClickHandler onClick = (event) -> {
            //If the player clicks on an item not on row 6, set cursor to that item
            Debug.log("Clicked on item "+event.getSlot());
            if (event.getSlot() < 45) {
                //set cursor 1 tick later to avoid bug with event cancelling
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ((Player) sender).setItemOnCursor(event.getCurrentItem());
                    }
                }.runTaskLater(WhMain.getPlugin(), 1);
            }
        };

        menu.open((Player) sender, onClick);
        return true;
    }

}
