package com.bonkle.whatever;

import com.bonkle.whatever.BlockAPI.CustomBlock;
import com.bonkle.whatever.BlockAPI.CustomBlockEvents;
import com.bonkle.whatever.ItemAPI.CustomItemEvents;
import com.bonkle.whatever.MenuAPI.InventoryMenuEvents;
import com.bonkle.whatever.RegisterAPI.Register;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhMain extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {

        plugin = this;
        //Debug.enable(plugin);

        CustomBlock.setCustomBlocksMetadata();

        Register.eventHandler(WhMain.plugin, new InventoryMenuEvents());
        Register.eventHandler(WhMain.plugin, new CustomItemEvents());
        Register.eventHandler(WhMain.plugin, new CustomBlockEvents());

        Commands.init();

    }

    @Override
    public void onDisable() { }

}
