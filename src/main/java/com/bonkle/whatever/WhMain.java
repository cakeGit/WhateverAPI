package com.bonkle.whatever;

import com.bonkle.whatever.BlockAPI.CustomBlock;
import com.bonkle.whatever.BlockAPI.CustomBlockEvents;
import com.bonkle.whatever.ItemAPI.CustomItemEvents;
import com.bonkle.whatever.MenuAPI.InventoryMenuEvents;
import com.bonkle.whatever.RegisterAPI.Register;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhMain extends JavaPlugin {

    //private static WhMain plugin;
    @Override
    public void onEnable() {
        //plugin = this;
        //Debug.enable(plugin);

        CustomBlock.setCustomBlocksMetadata();

        Register.eventHandler(WhMain.getPlugin(), new InventoryMenuEvents());
        Register.eventHandler(WhMain.getPlugin(), new CustomItemEvents());
        Register.eventHandler(WhMain.getPlugin(), new CustomBlockEvents());

        Commands.init();

    }

    public static JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(WhMain.class);
    }

    @Override
    public void onDisable() { }

}
