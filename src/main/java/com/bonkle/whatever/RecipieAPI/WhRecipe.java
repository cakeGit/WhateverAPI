package com.bonkle.whatever.RecipieAPI;

import com.bonkle.whatever.ItemAPI.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class WhRecipe {

    private boolean shapeless = false;
    private String pattern = null;
    private HashMap<String, Ingredient> ingredients = null;
    private Material[] shapelessIngredients = null;
    private ItemStack result = null;

    public WhRecipe(ItemStack result, String pattern, Object... ingredientsRaw) { //Shaped
        this.pattern = pattern;
        //Use every other object as an ingredient and the other one as the key
        if (ingredientsRaw.length % 2 != 0) { throw new IllegalArgumentException("Ingredients must be in pairs"); }
        for (int i = 0; i < ingredientsRaw.length; i += 2) {
            ingredients = new HashMap<>();
            ingredients.put((String) ingredientsRaw[i],
                    (ingredientsRaw[i + 1] instanceof String ?
                            new Ingredient(CustomItem.getCustomItem((String) ingredientsRaw[i + 1])) :
                            new Ingredient((Material) ingredientsRaw[i + 1])
            ));
        }
        this.result = result;
    }

    public WhRecipe(ItemStack result, Material... ingredients) { //Shapeless
        shapeless = true;
        shapelessIngredients = ingredients;
        this.result = result;
    }

    public ItemStack[] getExample() { // Returns an valid example of the recipe
        ItemStack[] example;
        if (shapeless) {
            example = new ItemStack[shapelessIngredients.length];
            for (int i = 0; i < example.length; i++) {
                example[i] = new ItemStack(shapelessIngredients[i]);
            }
        } else {
            example = new ItemStack[pattern.length()];
            for (int i = 0; i < example.length; i++) {
                Ingredient ingredient = ingredients.get(pattern.substring(i, i + 1));
                example[i] = (ingredient.isCustomItem() ?
                        ingredient.getCustomItem().generateItemStack() :
                        new ItemStack(ingredient.getMaterial())
                );
            }
        }
        return example;
    }

    public Boolean checkRecipeMatch(ItemStack[] craftingGrid) {
        if (shapeless) {
            ArrayList<Material> givenIngredients = new ArrayList<>();
            for (ItemStack item : craftingGrid) {
                if (item != null) {
                    givenIngredients.add(item.getType());
                }
            }
            if (givenIngredients.size() != shapelessIngredients.length) { return false; } //Quick check to save time

            for (Material ingredient : shapelessIngredients) {
                if (!givenIngredients.contains(ingredient)) { return false; }
                else { givenIngredients.remove(ingredient); }//Remove the ingredient so a shapeless recipe can have the same ingredient multiple times
            }

        } else {
            for (int i = 0; i < craftingGrid.length; i++) {
                Ingredient ingredient = ingredients.get(pattern.substring(i, i + 1));
                if ((
                        CustomItem.getCustomItem(craftingGrid[i]) == ingredient.getCustomItem() ||
                                (craftingGrid[i].getType() == ingredient.getMaterial() && !ingredient.isCustomItem())
                )) { return false; }
            }
        }
        return true;
    }

    public ItemStack getResult() {
        return result;
    }
}
