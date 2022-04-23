package com.bonkle.whatever.ItemAPI;

import com.bonkle.whatever.Debug;
import com.bonkle.whatever.ItemAPI.CustomItemHandlers.OnCustomItemUse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Locale;

public class CustomItem {

    private static final ArrayList<CustomItem> customItems = new ArrayList<>();

    /** <h2>Gets a list of all the custom items registered</h2>
     * @return ArrayList of CustomItems
     */ public static ArrayList<CustomItem> getCustomItems() { return customItems; }

    /** <h2>Gets a custom item by its full id</h2>
     * @param name Id of the custom item
     * @return CustomItem
     */ public static CustomItem getCustomItem(String name) {
        for (CustomItem customItem : customItems) {
            if (customItem.getFullId().equalsIgnoreCase(name)) return customItem;
        }
        return null;
    }

    /** <h2>Returns if the ItemStack is an instance of the CustomItem</h2>
     *
     * @param stack ItemStack to check
     * @param customItem CustomItem to check against
     * @return True if the ItemStack is an instance of the CustomItem
     */
    public static boolean stackMatchesCustom(ItemStack stack, CustomItem customItem) {
        return stack.getLore().get(0).equals(customItem.getFormattedFullId());
    }

    /** <h2>Gets a custom item by its lore identifier</h2>
     *
     * Returns null if no custom item is found
     *
     * @param stack Lore identifier of the custom item
     * @return CustomItem or null if not found
     */
    public static CustomItem getCustomItem(ItemStack stack) {
        Debug.log("Found " + customItems.size() + " custom items");
        return customItems.stream().filter(customItem -> stackMatchesCustom(stack, customItem)).findFirst().orElse(null);
    }

    //Custom item main data
    private final String name;
    private final String pluginNamespace;
    private final String id;
    private final Material material;
    private String[] categories = new String[0];
    private String[] defaultLore = new String[0];
    private ItemMeta meta;

    //Custom item functions
    private boolean edible = false;
    private float nutrition = 2.0F;
    private boolean weapon = false;
    private float damage = 2.0F;
    private WhItemStack eatResult = null;

    //Custom item Event Handlers
    private OnCustomItemUse onUse;
    private OnCustomItemUse onRightClick;
    private OnCustomItemUse onLeftClick;
    private OnCustomItemUse onAttackClick; //ToDo: Implement this
    private OnCustomItemUse onGenericLeftClick;

    public CustomItem(String name, String pluginNamespace) {
        this(name, pluginNamespace, name, Material.BLAZE_POWDER);
    }

    public CustomItem(String name, String pluginNamespace, String id) {
        this(name, pluginNamespace, id, Material.BLAZE_POWDER);
    }

    public CustomItem(String name, String pluginNamespace, String id, Material material) {
        String parsedId = id.toLowerCase(Locale.ROOT)
                .replace(" ", "_")
                .replace(":", "-");

        for (CustomItem customItem : customItems) {
            if (customItem.getFullId().equals(pluginNamespace + ":" + parsedId)) {
                throw new IllegalArgumentException("Custom item with id " + pluginNamespace + ":" + parsedId + " already exists!");
            }
        }

        this.name = name;
        this.id = parsedId;
        this.pluginNamespace = pluginNamespace;
        this.material = material;

        //Add to CustomItem list
        customItems.add(this);
    }

    public WhItemStack generateItemStack() {

        //put the full id into an array followed by defaultlore
        String[] fullLore = new String[defaultLore.length + 1];
        fullLore[0] = getFormattedFullId();
        System.arraycopy(defaultLore, 0, fullLore, 1, fullLore.length - 1);

        return new WhItemStack(material)
                .setItemMetaRI(meta)
                .setLore(fullLore)
                .setName(name);

    }

    /** <h2>Returns the item id and namespace</h2>
     * @return Formatted full ID
     */ public String getFormattedFullId() { return ChatColor.DARK_GRAY + pluginNamespace + ":" + id; }


    /** <h2>Returns the item id and namespace</h2>
     * @return Full ID
     */ public String getFullId() { return pluginNamespace + ":" + id; }

    //Getters and setters

    /**<h2>Sets all generated item's meta</h2>
     * @param meta ItemMeta to set
     * @return CustomItem for chaining
     */ public CustomItem setMeta(ItemMeta meta) { this.meta = meta; return this; }

    /**<h2>Returns the item name</h2>
     * @return Name of the item
     */ public String getName() { return name; }

    /**<h2>Returns the item id</h2>
     * @return ID of the item
     */ public String getID() { return id; }

    /**<h2>Returns the item material</h2>
     * @return Material of the item
     */ public Material getMaterial() { return material; }

    /**<h2>Returns the item categories</h2>
     * @return Categories of the item
     */ public String[] getCategories() { return categories; }

    /**<h2>Sets the item categories</h2>
     * @param categories Categories of the item (Single string or array)
     * @return CustomItem for chaining
     */ public CustomItem setCategories(String... categories) { this.categories = categories; return this; }

    /**<h2>Returns if the item is in a specific category</h2>
     * @return If it is in the category
     */ public boolean isInCategory(String category) {
        for(String cat : categories) {
            if(cat.equalsIgnoreCase(category)) return true;
        }
        return false;
    }

    /**<h2>Returns the item default lore</h2>
     * @return Default lore of the item
     */ public String[] getDefaultLore() { return defaultLore; }

    /**<h2>Sets the item default lore</h2>
     * @param defaultLore Default lore of the item (Single string or array)
     */ public CustomItem setDefaultLore(String... defaultLore) { this.defaultLore = defaultLore; return this; }

    /**<h2>Returns if the item is edible</h2>
     * <b>THIS IS FOR IF IT IS SET VIA CODE AND REQUIRES ITEM TO BE EDIBLE NORMALLY</b>
     * @return If the item is edible
     */ public boolean isEdible() { return edible; }

    /**<h2>Sets the item edible</h2>
     * <b>THIS IS FOR IF IT IS SET VIA CODE AND REQUIRES ITEM TO BE EDIBLE NORMALLY</b>
     * @param edible If the item is edible
     */ public CustomItem setEdible(boolean edible) { this.edible = edible; return this; }

    public CustomItem setEatResult(WhItemStack eatResult) {
        this.eatResult = eatResult;
        return this;
    }

    public WhItemStack getEatResult() {
        return eatResult;
    }

    public CustomItem setEatConsume(boolean b) {
         if (b) {
             this.eatResult = new WhItemStack(Material.AIR);
         } else {
             this.eatResult = generateItemStack();
         }
         return this;
    }

    /**<h2>Returns the item's nutrition</h2>
     * @return The item's nutrition
     */ public float getNutrition() { return nutrition; }

    /**<h2>Sets the item's nutrition</h2>
     * @param nutrition The item's nutrition
     */ public CustomItem setNutrition(float nutrition) { this.nutrition = nutrition; return this; }

    /**<h2>Returns if the item is a weapon</h2>
     * @return If the item is a weapon
     */ public boolean isWeapon() { return weapon; }

    /**<h2>Sets the item as a weapon</h2>
     * @param weapon If the item is a weapon
     */ public CustomItem setWeapon(boolean weapon) { this.weapon = weapon; return this; }

    public void onUse(PlayerInteractEvent event) {
        if(onUse != null) onUse.run(event);
    }

    /**<h2>Sets the item's onUse handler</h2>
     * @param onUse The event to run when the item is used
     */ public CustomItem setOnUse(OnCustomItemUse onUse) { this.onUse = onUse; return this; }

    public void onRightClick(PlayerInteractEvent event) {
        if(onRightClick != null) onRightClick.run(event);
    }

    /**<h2>Sets the item's onRightClick handler</h2>
     * @param onRightClick The event to run when the item is right clicked
     */ public CustomItem setOnRightClick(OnCustomItemUse onRightClick) { this.onRightClick = onRightClick; return this; }

    public void onLeftClick(PlayerInteractEvent event) {
        if(onLeftClick != null) onLeftClick.run(event);
        onGenericLeftClick(event);
    }

    /**<h2>Sets the item's onLeftClick handler</h2>
     * @param onLeftClick The event to run when the item is left clicked
     */ public CustomItem setOnLeftClick(OnCustomItemUse onLeftClick) { this.onLeftClick = onLeftClick; return this; }

    public void onGenericLeftClick(PlayerInteractEvent event) {
        if(onGenericLeftClick != null) onGenericLeftClick.run(event);
    }

    /**<h2>Sets the item's onLeftClick handler</h2>
     * @param onGenericLeftClick The event to run when the item is left clicked
     */ public CustomItem setOnGenericLeftClick(OnCustomItemUse onGenericLeftClick) { this.onGenericLeftClick = onGenericLeftClick; return this; }

}
