package com.bonkle.whatever.MenuAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryMenuEvents implements Listener {

    protected static ArrayList<OpenInventory> openInventories = new ArrayList<>();

    private static OpenInventory getOpenInventory(Inventory inv) {
        for (OpenInventory openInventory : openInventories) {
            if (openInventory.getInventory() == inv) {
                return openInventory;
            }
        }
        return null;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //allow dropping item into inventory
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) { return; } //cancel if player inventory
        OpenInventory openInventory = getOpenInventory(event.getInventory());
        if (openInventory != null) {
            event.setCancelled(true);
            if (openInventory.getOnClick() != null) {
                openInventory.getOnClick().onClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        OpenInventory openInventory = getOpenInventory(event.getInventory());
        if (openInventory != null) {
            if (openInventory.getOnClose() != null) {
                openInventory.getOnClose().onClose(event);
            }
        }
    }

}
