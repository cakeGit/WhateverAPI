package com.bonkle.whatever.ItemAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**<h1 color="red">INTERNAL - DO NOT USE</h1>*/
@ApiInternal
public class SpecificCustomItemEventHandler implements Listener {

    private final CustomItem customItem;

    @ApiInternal
    public SpecificCustomItemEventHandler(CustomItem customItem) { this.customItem = customItem; }

    private boolean ItemStackMatchesCustom(ItemStack itemStack) {
        if (itemStack == null) return false;
        try {
            return Objects.equals(
                    itemStack.getLore().get(0),
                    customItem.getFormattedFullId()
            );
        } catch (NullPointerException e) {
            return false;
        }

    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        try {
            event.getItem();
        } finally {
            if (ItemStackMatchesCustom(event.getItem())) {
                customItem.onUse(event);

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

            if (ItemStackMatchesCustom(player.getInventory().getItemInMainHand())) {
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
