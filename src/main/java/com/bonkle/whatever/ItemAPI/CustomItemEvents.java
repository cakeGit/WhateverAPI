package com.bonkle.whatever.ItemAPI;

import com.bonkle.whatever.RecipieAPI.WhRecipe;
import com.bonkle.whatever.Debug;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        try {
            event.getItem();
        } finally {
            CustomItem customItem = CustomItem.getCustomItem(event.getItem());

            if (customItem != null) {
                customItem.onUse(event);
                event.setCancelled(true);

                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    customItem.onLeftClick(event);
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    customItem.onRightClick(event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            CustomItem customItem = CustomItem.getCustomItem(player.getItemInHand());

            if (customItem != null) {
                customItem.onGenericLeftClick(
                        new PlayerInteractEvent(
                                player,
                                Action.LEFT_CLICK_AIR,
                                player.getInventory().getItemInMainHand(),
                                null, null
                        ));
            }
        }
    }

}
