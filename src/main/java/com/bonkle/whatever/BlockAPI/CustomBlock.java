package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockArmourStandBreak;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockBreak;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockInteract;
import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.WhMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

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
        for (World world : WhMain.getPlugin().getServer().getWorlds()) {
            for (ArmorStand as : world.getEntitiesByClass(ArmorStand.class)) {
                if (as.hasMetadata("customBlock")) {
                    Location asLoc = as.getLocation();
                    Location blockLoc = asLoc.clone().add(0,1,0);
                    if (blockLoc.getBlock().getType() == Material.BARRIER) {
                        blockLoc.getBlock().setMetadata("customBlock", new FixedMetadataValue(WhMain.getPlugin(), as.getCustomName()));
                    }
                }
            }
        }
    }

    private String name;
    private String namespace;
    private String id;

    private CustomItem linkedItem;

    private OnCustomBlockBreak onBreak;
    private OnCustomBlockInteract onInteract;
    private OnCustomBlockArmourStandBreak onArmourStandBreak;

    private double[] offset = new double[] {0,0,0};
    private double[] defaultOffset = new double[] {0.5,-1 + 1.0/16.0,0.5};
    private RotatableType rotatableType = RotatableType.NONE;
    private double rotationIncrement = 90;

    private boolean solid = true;
    private boolean drops = true;
    private boolean rightClickPickUp = false;

    public CustomBlock(String name, String namespace) {
        construct(name, namespace, name, Material.GOLD_BLOCK, null);
    }

    public CustomBlock(String name, String namespace, Material linkedItemMaterial) {
        construct(name, namespace, name, linkedItemMaterial, null);
    }

    public CustomBlock(String name, String namespace, CustomItem linkedCustomItem) {//LINKED CUSTOM ITEM'S ONRIGHTCLICK IS OVERWRITTEN
        construct(name, namespace, name, Material.GOLD_BLOCK, linkedCustomItem);
    }

    public CustomBlock(String name, String namespace, String id) {
        construct(name, namespace, id, Material.GOLD_BLOCK, null);
    }

    public CustomBlock(String name, String namespace, String id, Material linkedItemMaterial) {
        construct(name, namespace, id, linkedItemMaterial, null);
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
                                .clone().add(defaultOffset[0], defaultOffset[1], defaultOffset[2])
                                .add(offset[0],offset[1],offset[2]), //Shifted because the block will be 1/16th of a block too low because pivot
                        EntityType.ARMOR_STAND
                );
                if (rotatableType != RotatableType.NONE) {//Stuff which i did because do a good job naughty boy
                    if (rotatableType == RotatableType.DIRECTIONAL) {
                        //Make it face player along x axis, snap to rotationIncrement degree increments based on player direction
                        as.setHeadPose(new EulerAngle(0, Math.toRadians(Math.round(event.getPlayer().getLocation().getYaw() / rotationIncrement) * rotationIncrement ),
                                0));
                    } else if (rotatableType == RotatableType.FULL) {
                        //Make it face player along x axis AND y axis (based on side of block), snap to rotationIncrement degree increments based on player direction
                        double z=0;
                        double x=90;
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

                        as.setHeadPose(new EulerAngle(Math.toRadians(z), Math.toRadians(Math.round(event.getPlayer().getLocation().getYaw() / rotationIncrement) * rotationIncrement ),
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
                    world.getBlockAt(newBlockLoc).setMetadata("customBlock", new FixedMetadataValue(WhMain.getPlugin(), getFullId()));
                }

            }
        });
    }

    @ApiInternal
    public boolean onBreak(BlockBreakEvent event, ArmorStand as, boolean forceDisableDrops) { //Recives a potentialy real block break event so it returns if the true event should be cancelled which can be used elsewhere
        event.setDropItems(false);
        //Drop item at location
        if (drops && !forceDisableDrops) {
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), generateItemStack());
        }
        event.getBlock().removeMetadata("customBlock", WhMain.getPlugin());

        //Kill armor stand
        if (as == null) {
            ArmorStand BlockAs = (ArmorStand) event.getBlock().getWorld().getNearbyEntities(
                            event.getBlock().getLocation().clone().add(0.5,-1 + 1.0/16.0,0.5),
                            0.2, 0.2, 0.2
                    ).stream().filter(entity -> entity instanceof ArmorStand && entity.getLocation().equals(event.getBlock().getLocation().clone().add(0.5,-1 + 1.0/16.0,0.5)))
                    .findFirst().orElse(null);
            BlockAs.remove();
        } else {
            as.remove();
        }

        if (onBreak != null) {
            onBreak.run(event);
        }
        if (onArmourStandBreak != null && as != null) {
            Debug.log("Running onArmourStandBreak");
            onArmourStandBreak.run(event, as);
        }
        return event.isCancelled(); //Return val from onBreak
    }

    @ApiInternal
    public void onInteract(Player player, Location location, ArmorStand armorStand) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack genItem = generateItemStack();

        try {
            if (rightClickPickUp && (mainHand.getType() == Material.AIR ||
                    (Objects.equals(mainHand.getLore().get(0), linkedItem.getFormattedFullId()) && mainHand.getMaxStackSize() <= genItem.getMaxStackSize())
            )) {
                if (mainHand.getType() == Material.AIR) {
                    player.getInventory().setItemInMainHand(genItem);
                } else {
                    mainHand.setAmount(mainHand.getAmount() + generateItemStack().getAmount());
                }
                player.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                //Run block break event
                onBreak(new BlockBreakEvent(location.getBlock(), player), armorStand, true);
            }
        } catch (NullPointerException ignored) { }

        if (onInteract != null) {
            onInteract.run(player, location, armorStand);
        }
    }

    @ApiInternal
    private static void addCustomBlockToList(CustomBlock customBlock) {
        customBlocks.add(customBlock);
        Debug.log("Added custom block " + customBlock.getFullId() + " length: " + customBlocks.size());
    }

    public CustomBlock loadBlockOptions(BlockOptionsLambda blockOptionsLambda) {
        blockOptionsLambda.run(this);
        return this;
    }

    public static CustomBlock getCustomBlock(String customBlock) {
        return customBlocks.stream().filter(c -> c.getFullId().equals(customBlock)).findFirst().orElse(null);
    }

    public ItemStack generateItemStack() {
        return linkedItem.generateItemStack();
    }

    //Setters

    public CustomBlock setRotatableType(RotatableType directional) {
        this.rotatableType = directional;
        return this;
    }

    public CustomBlock setRotationIncrement(double rotationIncrement) {
        this.rotationIncrement = Math.toRadians(rotationIncrement);
        return this;
    }

    public CustomBlock setOnBreak(OnCustomBlockBreak onBreak) {
        this.onBreak = onBreak;
        return this;
    }

    public CustomBlock setOnInteract(OnCustomBlockInteract onInteract) {
        this.onInteract = onInteract;
        return this;
    }

    public CustomBlock setOnArmourStandBreak(OnCustomBlockArmourStandBreak onArmourStandBreak) {
        this.onArmourStandBreak = onArmourStandBreak;
        return this;
    }

    public CustomBlock setSolid(boolean solid) {
        this.solid = solid;
        return this;
    }

    public CustomBlock setOffset(double x, double y, double z) {
        this.offset = new double[]{x,y,z};
        return this;
    }

    public CustomBlock setDefaultYOffset(boolean defaultOffset) {
        if (!defaultOffset) {
            this.defaultOffset[1] = 0;
        }
        return this;
    }

    public CustomBlock setRightClickPickUp(boolean rightClickPickUp) {
        this.rightClickPickUp = rightClickPickUp;
        return this;
    }

    /**<h2>Sets if the block will drop items when broken</h2>
     * @param drops If the block drops items when broken
     */ public CustomBlock setDrops(boolean drops) {
        this.drops = drops;
        return this;
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

    public boolean getRightClickPickUp() {
        return rightClickPickUp;
    }

    public boolean isSolid() {
        return solid;
    }

    @Deprecated //Unfinished
    public ItemStack getDrops() { return linkedItem.generateItemStack(); }

}
