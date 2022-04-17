package com.bonkle.whatever.MenuAPI;

import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.WhItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryMenu implements Menu {

    private String name;
    private ItemStack[][] rows = new ItemStack[6][9];

    public InventoryMenu(String name) {
        this.name = name;
        Debug.log("Created  InventoryMenu: " + name);
    }

    public InventoryMenu setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryMenu setAllRows(ItemStack[][] rows) {
        this.rows = rows;
        return this;
    }

    public InventoryMenu setItemAt(int row, int column, ItemStack item) {
        rows[row-1][column-1] = item;
        return this;
    }

    public InventoryMenu setIndex(int index, ItemStack item) {
        rows[Math.floorDiv(index, 9)][index % 9] = item;
        return this;
    }

    public InventoryMenu setIndexes(ItemStack item, int... index) {
        for (int i : index) {
            Debug.log("Setting index " + i + " to " + item.getType().name());
            setIndex(i, item);
        }
        return this;
    }

    public InventoryMenu setRow(int row, WhItemStack item) {
        setRow(row, item.repeat(9));
        return this;
    }

    public InventoryMenu setRow(int row, ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            rows[row][i] = items[i];
        }
        Debug.log("Set row " + row + " to " + items[items.length-1].getType().name());
        return this;
    }

    public InventoryMenu setRow(int row, ArrayList<ItemStack> items) {
        setRow(row, items.toArray(new ItemStack[0]));
        return this;
    }

    public InventoryMenu setRows(WhItemStack[] items, int... rows) {
        for (int row : rows) {
            setRow(row, items);
        }
        return this;
    }

    public InventoryMenu setBorder(int borderHeight, ItemStack item, boolean edges) {
        //set top and bottom at 0 and borderHeight-1 to the item
        setRows(((WhItemStack) item).repeat(9), 0, borderHeight - 1);
        //set sides to the item
        if (edges) {
            for (int i = 1; i < borderHeight - 1; i++) {
                setIndexes(item, (i * 9), 8 + (i * 9));
            }
        }

        return this;
    }

    public InventoryMenu setFooter(int footerRow, ItemStack item) {
        Debug.log("Setting footer row " + footerRow + " to " + item.getType().name());
        setRow(footerRow-1, ((WhItemStack) item).repeat(9));
        return this;
    }

    public InventoryMenu setSidebar(int index, ItemStack ItemStack) {
        for (int i = 0; i < 6; i++) {
            rows[i][index] = ItemStack;
        }
        return this;
    }

    @Override
    public Inventory open(Player player, MenuClickHandler onClick, MenuCloseHandler onClose) {
        Debug.log("Opening InventoryMenu: " + name);

        //look for row index of last non-null item - loop backwards and exit when we find a non-null item
        Debug.log("Building Menu: " + name);
        int lastRow = 0;
        for (int i = rows.length - 1; i >= 0; i--) {
            for (int j = 0; j < rows[i].length; j++) {
                if (rows[i][j] != null) {
                    lastRow = i;
                    break;
                }
            }
            if (lastRow != 0) {
                break;
            }
        }
        Debug.log("Last row is " + lastRow);

        Inventory bukkitInventory = player.getServer().createInventory(player, 9 * (lastRow + 1), name);
        for (int row = 0; row <= lastRow; row++) {
            for (int column = 0; column < 9; column++) {
                bukkitInventory.setItem(row * 9 + column, rows[row][column]);
            }
        }

        Debug.log("Opening Menu: " + name);
        player.openInventory(bukkitInventory);

        //Register the OpenInventory
        InventoryMenuEvents.openInventories.add(
                new OpenInventory(bukkitInventory, onClick, onClose)
        );

        return bukkitInventory;
    }

    @Override public Inventory open(Player player, MenuClickHandler onClick) { return open(player, onClick, null); }
    @Override public Inventory open(Player player) { return open(player, null, null); }

}
