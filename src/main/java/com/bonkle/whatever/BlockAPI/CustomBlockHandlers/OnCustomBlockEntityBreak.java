package com.bonkle.whatever.BlockAPI.CustomBlockHandlers;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.BlockBreakEvent;

public interface OnCustomBlockEntityBreak { void run(BlockBreakEvent generatedEvent, ArmorStand armorStand); }
