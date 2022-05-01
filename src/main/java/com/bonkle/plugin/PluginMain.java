package com.bonkle.plugin;

import com.bonkle.whatever.BlockAPI.AsBlockOptions;
import com.bonkle.whatever.BlockAPI.CustomAsBlock;
import com.bonkle.whatever.BlockAPI.CustomBlock;
import com.bonkle.whatever.BlockAPI.RotatableType;
import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.CustomItem;
import com.bonkle.whatever.RegisterAPI.Register;
import com.bonkle.whatever.WhMain;
import org.bukkit.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PluginMain {

    public static void init() {



        Register.eventHandler(WhMain.plugin, new PluginEvents());

    }

}
