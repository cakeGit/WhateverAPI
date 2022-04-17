package com.bonkle.whatevertest;

import com.bonkle.whatever.BlockAPI.CustomBlock;
import com.bonkle.whatever.ItemAPI.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestMain {
    public static void init() {

        //create some custom items
        String ns="example";
        CustomItem item1 = new CustomItem("Item 1", ns,"item1")
                .setOnUse(event -> {
                    Player player = event.getPlayer();
                    player.sendMessage("Item 1 used");
                })
                .setOnRightClick(event -> {
                    Player player = event.getPlayer();
                    player.sendMessage("Item 1 right clicked");
                })
                .setOnLeftClick(event -> {
                    Player player = event.getPlayer();
                    player.sendMessage("Item 1 left clicked");
                });
        CustomItem item2 = new CustomItem("Item 2", ns, "item2");
        CustomItem item3 = new CustomItem("Item 3", ns, "item3");

        CustomBlock block1 = new CustomBlock("Block 1", ns, "test_block");

        //give everyone some items
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(item1.generateItemStack());
            player.getInventory().addItem(item2.generateItemStack());
            player.getInventory().addItem(item3.generateItemStack());
            player.getInventory().addItem(block1.generateItemStack());
        }

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
