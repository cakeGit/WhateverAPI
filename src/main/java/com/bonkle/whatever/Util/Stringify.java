package com.bonkle.whatever.Util;

import org.bukkit.Location;

public class Stringify {

    public static String location(Location location) {// Round a bit for better readability
        return "(" + Math.round(location.getX()*100)/100 + ", " + Math.round(location.getY()*100)/100 + ", " + Math.round(location.getZ()*100)/100 + ")";
    }

}
