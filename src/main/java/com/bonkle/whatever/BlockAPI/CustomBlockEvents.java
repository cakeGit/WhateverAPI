package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.Debug;
import com.bonkle.whatever.WhMain;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CustomBlockEvents implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) { //Stops killing the custom block armour stand
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();

            for (CustomBlock cb : CustomBlock.getCustomBlocks()) {
                if (armorStand.getCustomName().contains(cb.getFullId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) { //Handles custom block breaking
        if (event.getBlock().hasMetadata("customBlock")) {
            Debug.log(event.getBlock().getMetadata("customBlock").get(0).asString());

            event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation(),
                    CustomBlock.getCustomBlock(
                            event.getBlock().getMetadata("customBlock").get(0).asString()
                    ).generateItemStack()
            );

            event.getBlock().removeMetadata("customBlock", WhMain.plugin);
            //Kill armor stand
            event.getBlock().getWorld().getNearbyEntities(
                    event.getBlock().getLocation().clone().add(0.5,-1 + 1.0/16.0,0.5),
                    0.2, 0.2, 0.2
            ).stream().filter(entity -> entity instanceof ArmorStand && entity.getLocation().equals(event.getBlock().getLocation().clone().add(0.5,-1 + 1.0/16.0,0.5)))
                    .findFirst().orElse(null).remove();
        }
    }

}
