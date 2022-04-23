package com.bonkle.whatever.BlockAPI.CustomBlockHandlers;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface OnCustomBlockInteract { void run(Player player, Location location, @Nullable ArmorStand armorStand); }
