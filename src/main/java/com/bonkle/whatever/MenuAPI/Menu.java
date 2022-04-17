package com.bonkle.whatever.MenuAPI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Menu {
    Inventory open(Player player, MenuClickHandler onClick, MenuCloseHandler onClose);
    Inventory open(Player player, MenuClickHandler onClick);
    Inventory open(Player player);
}
