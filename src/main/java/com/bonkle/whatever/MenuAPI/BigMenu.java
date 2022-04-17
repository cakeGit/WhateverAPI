package com.bonkle.whatever.MenuAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import com.bonkle.whatever.ItemAPI.WhItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * <h2>Container for a big arraylist of menu items which are put into pages or scrollable menu.</h2>
 */
public class BigMenu  implements  Menu{ //Not really a menu but will have the same methods.

    private String name;
    private ArrayList<ArrayList<ItemStack>> contents = new ArrayList<>(); // [row][slot] Both Pages will display multiple rows
    private int position; //Page or scroll position
    private BigMenuType type; //Scroll or page

    public BigMenu(String name, BigMenuType type) {
        this.name = name;
        this.position = 0;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public BigMenu setRow(int row, ArrayList<ItemStack> items) {
        contents.add(row, items);
        return this;
    }

    public BigMenu dumpContents(ArrayList<ItemStack> items) {//TODO make this more efficient because it sets to null then thing idk i just know i did this badly pls fix ok thank you by i said goodby please go awwawy now ok im done
        //Will just put contents into the nearest non-null indexes
        int itemIndex = 0;
        int rowIndex = 0;
        while (true) {
            for (int slot = 0; slot < 9; slot++) {
                try {
                    contents.get(rowIndex);
                } catch (IndexOutOfBoundsException e) {
                    contents.add(rowIndex, new ArrayList<>());
                    //Fill with nulls
                    for (int i = 0; i < 9; i++) {
                        contents.get(rowIndex).add(null);
                    }
                }

                if (contents.get(rowIndex).get(slot) == null) {
                    contents.get(rowIndex).set(slot, items.get(itemIndex));
                    itemIndex++;
                    if (itemIndex >= items.size()) {
                        return this;
                    }
                }
            }
            rowIndex++;
        }
    }

    public BigMenuType getType() {
        return type;
    }

    private ItemStack[][] generatePageContents() {
        ItemStack[][] contents = new ItemStack[5][9];
        for (int row = 0; row < 5; row++) {
            for (int slot = 0; slot < 9; slot++) {
                try {
                    contents[row][slot] = this.contents.get(row+position).get(slot);
                } catch (IndexOutOfBoundsException e) {
                    contents[row][slot] = null;
                }
            }
        }
        return contents;
    }

    private ItemStack[] generateButtons() {
        ItemStack backButton;
        if (position > 0) {
            backButton = new WhItemStack(Material.ARROW, ChatColor.GREEN +""+ ChatColor.BOLD + "Previous page");
        } else {
            backButton = new WhItemStack(Material.STRUCTURE_VOID, "End of pages this way!");
        }

        ItemStack nextButton;
        if (position < contents.size() - 5) {
            nextButton = new WhItemStack(Material.ARROW, ChatColor.GREEN +""+ ChatColor.BOLD + "Next page");
        } else {
            nextButton = new WhItemStack(Material.STRUCTURE_VOID, "End of pages this way!");
        }
        return new ItemStack[]{backButton, nextButton};
    }

    @ApiInternal
    public InventoryMenu generateInv() {
        InventoryMenu menu = new InventoryMenu(name);

        ItemStack[][] pageContents = generatePageContents();
        for (int row = 0; row < 5; row++) {
            for (int slot = 0; slot < 9; slot++) {
                if (pageContents[row][slot] != null) {
                    menu.setItemAt(row+1, slot+1, pageContents[row][slot]);
                }
            }
        }

        //Set footer
        menu.setFooter(6, new WhItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));

        //Add buttons
        ItemStack[] buttons = generateButtons();

        menu.setItemAt(6, 2, buttons[0]);
        menu.setItemAt(6, 8, buttons[1]);

        return menu;

    }

    @Override
    public Inventory open(Player player, MenuClickHandler onClick, MenuCloseHandler onClose) {
        MenuClickHandler wrappedOnClick = (event) -> {
            //Check if clicked slot is either the 2nd or 8th slot in 6th row
            int clickedSlot = event.getSlot();
            boolean update = false;
            if (clickedSlot == 46) {
                if (position > 0) {
                    position-=(type == BigMenuType.PAGE ? 5 : 1);
                    update = true;
                }
            } else if (clickedSlot == 52) {
                if (position < contents.size() - 5) {
                    position+=(type == BigMenuType.PAGE ? 5 : 1);
                    update = true;
                }
            }
            if (update) {
                //Get new contents and set inventory to new contents
                ItemStack[][] pageContents = generatePageContents();
                for (int row = 0; row < 5; row++) {
                    for (int slot = 0; slot < 9; slot++) {
                        if (pageContents[row][slot] != null) {
                            event.getInventory().setItem(row * 9 + slot, pageContents[row][slot]);
                        } else {
                            event.getInventory().setItem(row * 9 + slot, new ItemStack(Material.AIR));
                        }
                    }
                }

                ItemStack[] buttons = generateButtons();

                event.getInventory().setItem(5 * 9 + 1, buttons[0]);
                event.getInventory().setItem(5 * 9 + 7, buttons[1]);
            }
            if (onClick != null) {
                onClick.onClick(event);
            }
        };


        return generateInv().open(player, wrappedOnClick, onClose);
    }

    @Override
    public Inventory open(Player player, MenuClickHandler onClick) {
        return open(player, onClick, null);
    }

    @Override
    public Inventory open(Player player) {
        return open(player, null, null);
    }
}
