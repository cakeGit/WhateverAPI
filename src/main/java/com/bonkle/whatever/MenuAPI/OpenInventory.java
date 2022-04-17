package com.bonkle.whatever.MenuAPI;

import org.bukkit.inventory.Inventory;

public class OpenInventory {

    private final Inventory inventory;
    private final MenuClickHandler onClick;
    private final MenuCloseHandler onClose;

    public OpenInventory(Inventory inventory, MenuClickHandler onClick, MenuCloseHandler onClose) {
        this.inventory = inventory;
        this.onClick = onClick;
        this.onClose = onClose;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MenuClickHandler getOnClick() {
        return onClick;
    }

    public MenuCloseHandler getOnClose() {
        return onClose;
    }

}
