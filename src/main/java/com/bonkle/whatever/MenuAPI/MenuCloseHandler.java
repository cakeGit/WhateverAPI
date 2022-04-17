package com.bonkle.whatever.MenuAPI;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface MenuCloseHandler {
    void onClose(InventoryCloseEvent event);
}
