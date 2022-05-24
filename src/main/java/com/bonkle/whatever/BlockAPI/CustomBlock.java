package com.bonkle.whatever.BlockAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockEntityBreak;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockBreak;
import com.bonkle.whatever.BlockAPI.CustomBlockHandlers.OnCustomBlockInteract;
import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.Util.Stringify;
import com.bonkle.whatever.WhMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class CustomBlock {

    protected static ArrayList<CustomBlock> customBlocks = new ArrayList<>();

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
                    Location blockLoc = asLoc.clone().add(0, 1, 0);
                    if (blockLoc.getBlock().getType() == Material.BARRIER) {
                        blockLoc.getBlock().setMetadata("customBlock", new FixedMetadataValue(WhMain.plugin, as.getCustomName()));
                    }
                }
            }
        }
    }

    protected String name;
    protected String namespace;
    protected String id;

    protected CustomItem linkedItem;

    protected OnCustomBlockBreak onBreak;
    protected OnCustomBlockInteract onInteract;
    protected OnCustomBlockEntityBreak onEntityBreak;

    protected double[] offset = new double[]{0, 0, 0};
    protected RotatableType rotatableType = RotatableType.NONE;
    protected double rotationIncrement = 90;

    protected boolean solid = true;
    protected boolean drops = true;
    protected boolean rightClickPickUp = false;

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

        this.linkedItem = Objects.requireNonNullElseGet(linkedItem, () -> new CustomItem(name, namespace, id, linkedItemMaterial));

        this.linkedItem.setOnRightClick((event) -> {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Location newBlockLoc = event.getClickedBlock().getLocation()
                        .add(event.getBlockFace().getDirection());
                World world = newBlockLoc.getWorld();

                //Check if there is an entity or block in the way
                if (world.getBlockAt(newBlockLoc).getType() != Material.AIR) {
                    Debug.log("Cannot place block " + this.getFullId() + " at " + newBlockLoc.toString() + " because there is a block in the way!");
                    return;
                }
                if (newBlockLoc.getNearbyEntities(0.4, 0.4, 0.4).size() > 0) {
                    Debug.log("There is an entity in the way!");
                    return;
                }

                Debug.log("newBlockLoc: " + Stringify.location(newBlockLoc));

                //Summon the armour stand
                ItemFrame itemFrame = (ItemFrame) world.spawnEntity(
                        newBlockLoc
                                .clone()
                                .add(offset[0], offset[1], offset[2]),
                        EntityType.ITEM_FRAME
                );

                //Set the item and other details
                itemFrame.setItem(this.linkedItem.generateItemStack());
                itemFrame.setCustomName(getFullId());
                itemFrame.setInvulnerable(true);
                //Make it invisible
                itemFrame.setVisible(false);

                //set the orentation to the block face
                itemFrame.setFacingDirection(event.getBlockFace(), true);

                //Place the block

                if (solid) {
                    world.getBlockAt(newBlockLoc).setType(Material.BARRIER);
                    world.getBlockAt(newBlockLoc).setMetadata("customBlock", new FixedMetadataValue(WhMain.plugin, getFullId()));
                }

            }
        });
    }

    @ApiInternal
    public boolean onBreak(BlockBreakEvent event, Entity brokenEntity, boolean forceDisableDrops) { //Recives a potentialy real block break event so it returns if the true event should be cancelled which can be used elsewhere
        event.setDropItems(false);
        //Drop item at location
        if (drops && !forceDisableDrops) {
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), generateItemStack());
        }
        event.getBlock().removeMetadata("customBlock", WhMain.plugin);

        //Kill armor stand
        if (brokenEntity == null) {
            Entity BlockAs = event.getBlock().getWorld().getNearbyEntities(
                            event.getBlock().getLocation().clone().add(offset[0]+0.5, offset[1], offset[2]+0.5),
                    0.2, 0.2, 0.2
                    ).stream().filter(entity -> entity instanceof ItemFrame).findFirst().orElse(null);

            BlockAs.remove();
        } else {
            brokenEntity.remove();
        }

        if (onBreak != null) {
            onBreak.run(event);
        }
        /*if (onEntityBreak != null && brokenEntity != null) {
            Debug.log("Running onArmourStandBreak");
            onArmourStandBreak.run(event, (ArmorStand) brokenEntity);
        }*/
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
        } catch (NullPointerException ignored) {
        }

        if (onInteract != null) {
            onInteract.run(player, location, armorStand);
        }
    }

    @ApiInternal
    public static void addCustomBlockToList(CustomBlock customBlock) {
        customBlocks.add(customBlock);
        Debug.log("Added custom block " + customBlock.getFullId() + " length: " + customBlocks.size());
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

    public CustomBlock setOnEntityBreak(OnCustomBlockEntityBreak setOnEntityBreak) {
        this.onEntityBreak = setOnEntityBreak;
        return this;
    }

    public CustomBlock setSolid(boolean solid) {
        this.solid = solid;
        return this;
    }

    public CustomBlock setOffset(double x, double y, double z) {
        this.offset = new double[]{x, y, z};
        return this;
    }

    public CustomBlock setRightClickPickUp(boolean rightClickPickUp) {
        this.rightClickPickUp = rightClickPickUp;
        return this;
    }

    /**
     * <h2>Sets if the block will drop items when broken</h2>
     *
     * @param drops If the block drops items when broken
     */
    public CustomBlock setDrops(boolean drops) {
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
    public ItemStack getDrops() {
        return linkedItem.generateItemStack();
    }

}
