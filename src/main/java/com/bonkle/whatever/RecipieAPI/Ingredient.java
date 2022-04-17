package com.bonkle.whatever.RecipieAPI;

import com.bonkle.whatever.Annotations.ApiInternal;
import com.bonkle.whatever.ItemAPI.CustomItem;
import org.bukkit.Material;

@ApiInternal
public class Ingredient {

    private boolean isCustomItem = false;
    private CustomItem customItem;
    private Material material = null;

    public Ingredient(CustomItem customItem) {
        this.isCustomItem = true;
        this.customItem = customItem;
    }

    public Ingredient(Material material) {
        this.material = material;
    }

    public boolean isCustomItem() {
        return isCustomItem;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    public Material getMaterial() {
        return material;
    }

}
