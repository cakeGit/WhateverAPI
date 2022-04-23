package com.bonkle.whatever.BlockAPI.CustomBlockHandlers;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.BlockBreakEvent;

public interface OnCustomBlockArmourStandBreak { void run(BlockBreakEvent generatedEvent, ArmorStand armorStand); }
