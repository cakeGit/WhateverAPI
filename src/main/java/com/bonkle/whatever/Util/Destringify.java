package com.bonkle.whatever.Util;

import org.bukkit.Location;
import org.bukkit.World;

public class Destringify {

    public static Location location(String location, World world) {
        String[] split = location.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i]=split[i].replace("(", "").replace(")", "").replace(" ", "");
        }
        return new Location(world, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

}
