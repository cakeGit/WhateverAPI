package com.bonkle.whatever.Switches;

import org.bukkit.block.BlockFace;

public class Switches {

    public int[] blockFaceToVec2(BlockFace blockFace) {
        int[] vec2 = new int[2];
        switch (blockFace) {
            case UP:
                vec2[1] = 1;
                break;
            case DOWN:
                vec2[1] = -1;
                break;
            case NORTH:
                vec2[0] = 1;
                break;
            case SOUTH:
                vec2[0] = 2;
                break;
            case EAST:
                vec2[0] = 3;
                break;
            case WEST:
                vec2[0] = 4;
                break;
        }
        return vec2;
    }

}
