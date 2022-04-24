package com.bonkle.plugin;

import com.bonkle.whatever.BlockAPI.BlockOptionsLambda;
import com.bonkle.whatever.BlockAPI.CustomBlock;
import com.bonkle.whatever.BlockAPI.RotatableType;
import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.RegisterAPI.Register;
import com.bonkle.whatever.WhMain;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PluginMain {

    private static void setFirstLore(ItemMeta im, String lore) {
        List<String> lores = im.getLore();
        lores.set(0, lore);
        im.setLore(lores);
    }

    public static void init() { //Currently plugin jam stuff

        String ns = "pluginjam";
        Material mat = Material.NETHERITE_BLOCK;

        new CustomBlock("Big Truss", ns, mat);

        new CustomBlock("Small Truss", ns, mat)
                .setRotatableType(RotatableType.FULL);

        new CustomBlock("Ribbon", ns, Material.RED_CARPET)
                .setOffset(0, 0.2, 0)
                .setSolid(false)
                .setRotatableType(RotatableType.DIRECTIONAL)
                .setDrops(false)
                .setOnArmourStandBreak((event, as) -> {
                    Debug.log("Ribbon onBreak");
                    //Get direction of armourstand and spawn redstone particles 2 blocks away in both ways perpendicular to the direction
                    double yaw = as.getHeadPose().getY();

                    double x = Math.cos(yaw);
                    double z = Math.sin(yaw);

                    //Make particle data for redstone particles
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(200, 20, 20), 1.0F);

                    final int[] i = {0};
                    int repeat = 2;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (i[0] >= repeat) {
                                this.cancel();
                            }
                            i[0]++;
                            int density = 40;
                            double range = 5.5;
                            float yOffset = 1.5F;

                            for (int i = 1; i < density+1; i++) { //Spawn particles
                                double v = range * (i / (density+0F) ) - (range / 2.0);
                                as.getWorld().spawnParticle(Particle.REDSTONE,
                                        as.getLocation().add(
                                                x * v,
                                                yOffset + Math.abs(-(range/2) + (range * ( i / (density+0F) )))/9,
                                                z * v
                                        ), 1, dustOptions);
                            }
                        }
                    }.runTaskTimer(WhMain.plugin, 0, 1);

                });

        BlockOptionsLambda genericPropOptions = (block) -> {
            block
                    .setSolid(false)
                    .setDefaultYOffset(false)
                    .setRotatableType(RotatableType.DIRECTIONAL);
        };

        new CustomBlock("Camera", ns, Material.CROSSBOW)
                .loadBlockOptions(genericPropOptions)
                .setRotationIncrement(30)
                .setOnInteract((player, location, armourStand) -> {
                    PluginEvents.cameraOperators.put(player, armourStand);
                    player.sendMessage(ChatColor.AQUA + "You are now operating this camera!" + ChatColor.DARK_GRAY + " (Press LShift to stop)");
                });

        new CustomBlock("Barrier", ns, Material.BARRIER)
                .loadBlockOptions(genericPropOptions);

        new CustomBlock("Coek Can", ns, Material.MILK_BUCKET)
                .loadBlockOptions(genericPropOptions)
                .setRightClickPickUp(true)
                .setRotationIncrement(30)
                .getLinkedItem()
                .setEatConsume(false);

        new CustomBlock("Pepis Can", ns, Material.MILK_BUCKET)
                .loadBlockOptions(genericPropOptions)
                .setRightClickPickUp(true)
                .setRotationIncrement(30);

        new CustomBlock("Studio Seat", ns, Material.BLAZE_POWDER)
                .loadBlockOptions(genericPropOptions);

        new CustomBlock("Popcorn", ns, Material.BREAD)
                .loadBlockOptions(genericPropOptions)
                .setRightClickPickUp(true)
                .setRotationIncrement(30);

        new CustomItem("Scissors", ns, "scissors-open")
                .setOnGenericLeftClick(event -> {
                    //Set first lore entry to ns+":scissors-closed"
                    ItemMeta meta = event.getItem().getItemMeta();
                    setFirstLore(meta, ChatColor.DARK_GRAY+ ns+":scissors-closed");
                    event.getItem().setItemMeta(meta);
                });
        new CustomItem("Scissors", ns, "scissors-closed")
                .setDefaultLore(ChatColor.AQUA + "Right click to open scissors")
                .setOnRightClick(event -> {
                    //Set first lore entry to ns+":scissors-open"
                    ItemMeta meta = event.getItem().getItemMeta();
                    setFirstLore(meta, ChatColor.DARK_GRAY+ ns+":scissors-open");
                    event.getItem().setItemMeta(meta);
                });

        Register.eventHandler(WhMain.plugin, new PluginEvents());

        //Stuff im going to add because copilot started using stuff which didnt
        //create some custom categories
        /*Category cat1 = new Category("cat1", "cat1 description");
        Category cat2 = new Category("cat2", "cat2 description");
        Category cat3 = new Category("cat3", "cat3 description");

        //add items to categories
        cat1.addItem(item1);
        cat1.addItem(item2);
        cat2.addItem(item3);*/
    }

}
