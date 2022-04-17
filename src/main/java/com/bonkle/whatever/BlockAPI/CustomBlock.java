package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.WhMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Locale;

public class CustomBlock {

    private static ArrayList<CustomBlock> customBlocks = new ArrayList<>();

    public static ArrayList<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }

    /**
     * Internal use only - Makes sure the custom block metadata for blocks isnt null as it is reset on server restart
     */
    @ApiInternal
    public static void setCustomBlocksMetadata() {
        //Because metadata gets wiped on reload, look for armourstands and set metadata of the block above (because thats how they are positioned)
        for (World world : WhMain.plugin.getServer().getWorlds()) {
            for (ArmorStand as : world.getEntitiesByClass(ArmorStand.class)) {
                if (as.hasMetadata("customBlock")) {
                    Location asLoc = as.getLocation();
                    Location blockLoc = asLoc.clone().add(0,1,0);
                    if (blockLoc.getBlock().getType() == Material.BARRIER) {
                        blockLoc.getBlock().setMetadata("customBlock", new FixedMetadataValue(WhMain.plugin, as.getCustomName()));
                    }
                }
            }
        }
    }

    private String name;
    private String namespace;
    private String id;

    private CustomItem linkedItem;

    private boolean solid = true;

    public CustomBlock(String name, String namespace) {
        this(name, namespace, name, Material.REDSTONE_BLOCK);
    }

    public CustomBlock(String name, String namespace, String id) {
        this(name, namespace, id, Material.REDSTONE_BLOCK);
    }

    public CustomBlock(String name, String namespace, String id, Material linkedItemMaterial) {
        String parsedId = id.toLowerCase(Locale.ROOT)
                .replace(" ", "_")
                .replace(":", "-");

        for (CustomBlock cb : customBlocks) {
            if (cb.getFullId().equals(namespace + ":" + parsedId)) {
                throw new IllegalArgumentException("Custom block with id " + namespace + ":" + id + " already exists!");
            }
        }

        this.name = name;
        this.namespace = namespace;
        this.id = parsedId;

        customBlocks.add(this);

        linkedItem = new CustomItem(name, namespace, id, linkedItemMaterial);
        linkedItem.setOnRightClick((event) -> {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.getPlayer().sendMessage("Placing custom block!");

                Location newBlockLoc = event.getClickedBlock().getLocation()
                        .add(event.getBlockFace().getDirection());
                World world = newBlockLoc.getWorld();

                world.getBlockAt(newBlockLoc).setType(Material.BARRIER);
                world.getBlockAt(newBlockLoc).setMetadata("customBlock", new FixedMetadataValue(WhMain.plugin, getFullId()));

                //Summon the armour stand
                ArmorStand as = (ArmorStand) world.spawnEntity(
                        newBlockLoc
                                .add(0.5,-1 + 1.0/16.0,0.5), //Shifted because the block will be 1/16th of a block too low because pivot
                        EntityType.ARMOR_STAND
                );

                //Set various properties
                as.setInvulnerable(true);
                as.setCanMove(false);
                as.setCustomName(getFullId());
            }
        });
    }

    public static CustomBlock getCustomBlock(String customBlock) {
        return customBlocks.stream().filter(c -> c.getFullId().equals(customBlock)).findFirst().orElse(null);
    }


    public ItemStack generateItemStack() {
        return linkedItem.generateItemStack();
    }

    // Getters

    public String getFullId() {
        return namespace + ":" + id;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    public CustomItem getLinkedItem() {
        return linkedItem;
    }

}
