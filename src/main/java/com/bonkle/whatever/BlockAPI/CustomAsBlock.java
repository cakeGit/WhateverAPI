package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.WhMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

import java.util.Locale;

@Deprecated
public class CustomAsBlock extends CustomBlock {

    protected double defaultYOffset = -1 + (1.0/16.0);
    protected double defaultXZOffset = 0.5;

    public CustomAsBlock(String name, String namespace) {
        super(name, namespace);
    }

    public CustomAsBlock(String name, String namespace, Material linkedItemMaterial) {
        super(name, namespace, linkedItemMaterial);
    }

    public CustomAsBlock(String name, String namespace, CustomItem linkedCustomItem) {
        super(name, namespace, linkedCustomItem);
    }

    public CustomAsBlock(String name, String namespace, String id) {
        super(name, namespace, id);
    }

    public CustomAsBlock(String name, String namespace, String id, Material linkedItemMaterial) {
        super(name, namespace, id, linkedItemMaterial);
    }

    void construct(String name, String namespace, String id, Material linkedItemMaterial, CustomItem linkedItem) {
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

        addCustomBlockToList(this);

        if (linkedItem != null) {
            this.linkedItem = linkedItem;
        } else {
            this.linkedItem = new CustomItem(name, namespace, id, linkedItemMaterial);
        }
        this.linkedItem.setOnRightClick((event) -> {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Location newBlockLoc = event.getClickedBlock().getLocation()
                        .add(event.getBlockFace().getDirection());
                World world = newBlockLoc.getWorld();

                //Summon the armour stand
                ArmorStand as = (ArmorStand) world.spawnEntity(
                        newBlockLoc
                                .clone().add(defaultXZOffset, defaultYOffset, defaultXZOffset)
                                .add(offset[0], offset[1], offset[2]), //Shifted because the block will be 1/16th of a block too low because pivot
                        EntityType.ARMOR_STAND
                );
                if (rotatableType != RotatableType.NONE) {//Stuff which i did because do a good job naughty boy
                    if (rotatableType == RotatableType.DIRECTIONAL) {
                        //Make it face player along x axis, snap to rotationIncrement degree increments based on player direction
                        as.setHeadPose(new EulerAngle(0, Math.toRadians(Math.round(event.getPlayer().getLocation().getYaw() / rotationIncrement) * rotationIncrement),
                                0));
                    } else if (rotatableType == RotatableType.FULL) {
                        //Make it face player along x axis AND y axis (based on side of block), snap to rotationIncrement degree increments based on player direction
                        double z = 0;
                        double x = 90;
                        switch (event.getBlockFace()) {
                            case UP:
                            case DOWN:
                                x = 0;
                                break;
                            case NORTH:
                                z = -90;
                                break;
                            case SOUTH:
                                z = 90;
                                break;
                            case EAST:
                                z = 180;
                                break;
                            case WEST:
                                z = 0;
                        }

                        as.setHeadPose(new EulerAngle(Math.toRadians(z), Math.toRadians(Math.round(event.getPlayer().getLocation().getYaw() / rotationIncrement) * rotationIncrement),
                                Math.toRadians(x)));
                    }
                }

                //Set various properties
                as.setCanMove(false);
                as.setCustomName(getFullId());

                //Place the block

                if (solid) {
                    as.setInvulnerable(true);
                    world.getBlockAt(newBlockLoc).setType(Material.BARRIER);
                    world.getBlockAt(newBlockLoc).setMetadata("customBlock", new FixedMetadataValue(WhMain.plugin, getFullId()));
                }

            }
        });
    }

    public CustomAsBlock setDefaultYOffset(boolean yOffset) {
        if (yOffset) {
            this.defaultYOffset = -1 + (1.0/16.0);
        } else {
            this.defaultYOffset = 0;
        }
        return this;
    }

    public CustomAsBlock loadBlockOptions(AsBlockOptions options) {
        options.run(this);
        return this;
    }

}