package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.Debug;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class CustomBlockEvents implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) { //Stops killing the custom block armour stand

        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            //loop all custom blocks
            for (CustomBlock cb : CustomBlock.getCustomBlocks()) {
                if (armorStand.getCustomName().contains(cb.getFullId()) && cb.isSolid()) {
                    event.setCancelled(true);
                    break;
                } else if (armorStand.getCustomName().contains(cb.getFullId()) && !cb.isSolid()) {
                    event.setCancelled(false);
                    Debug.log("EntityDamageByEntityEvent: " + event.getEntity().getCustomName() + " was damaged by " + ((Player) event.getDamager()).getDisplayName());
                    if (event.getFinalDamage() >= armorStand.getHealth() || ((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE) {
                        cb.onBreak(new BlockBreakEvent(
                                armorStand.getLocation().getBlock(),
                                (Player) event.getDamager()
                        ), armorStand, false);
                        Debug.log("CustomBlockEvents Custom block killed by player");
                    }
                    break;
                }
            }
        }

    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) { //Handles custom block armourstand death
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();

            for (CustomBlock cb : CustomBlock.getCustomBlocks()) {
                if (armorStand.getCustomName().contains(cb.getFullId())) {
                    event.getDrops().clear();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) { //Handles custom block breaking
        if (event.getBlock().hasMetadata("customBlock")) {
            CustomBlock cb = CustomBlock.getCustomBlock(event.getBlock().getMetadata("customBlock").get(0).asString());
            cb.onBreak(event, null, false);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) { //Handles custom block interaction
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().hasMetadata("customBlock")) {
                CustomBlock cb = CustomBlock.getCustomBlock(event.getClickedBlock().getMetadata("customBlock").get(0).asString());
                cb.onInteract(event.getPlayer(), event.getClickedBlock().getLocation(), null);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractAtEntityEvent event) { //Handles custom block interaction
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();

            for (CustomBlock cb : CustomBlock.getCustomBlocks()) {
                if (armorStand.getCustomName().contains(cb.getFullId()) && !cb.isSolid()) {
                    cb.onInteract(event.getPlayer(), event.getRightClicked().getLocation(), armorStand);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) { //Handles custom block interaction

    }
}
