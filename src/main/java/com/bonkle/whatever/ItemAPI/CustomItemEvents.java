package com.bonkle.whatever.ItemAPI;

import com.bonkle.whatever.RecipieAPI.WhRecipe;
import com.bonkle.whatever.Debug;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CustomItemEvents implements Listener {

    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {

        Debug.log("Looking for custom item in crafting inventory");

        CraftingInventory inventory = event.getInventory();
        ItemStack[] items = inventory.getMatrix();
        Debug.log("Items: " + items.length);

        for (ItemStack item : items) {
            if (item != null) {
                Debug.log("Item: " + item.getType().name());
                for (CustomItem customItem : CustomItem.getCustomItems()) {
                    Debug.log("Custom Item: " + customItem.getName());
                }
                if (CustomItem.getCustomItem(item) != null) {
                    Debug.log("Found custom item");
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    break;
                }
            }
        }

        Debug.log("Checking for custom recipe");

        WhRecipe testRecipe = new WhRecipe(new ItemStack(Material.STONE), Material.GUNPOWDER);

        if (testRecipe.checkRecipeMatch(items)) {
            Debug.log("Found custom recipe for " + testRecipe.getResult().getType().name());
            event.getInventory().setResult(testRecipe.getResult());
        }

    }

}
