package com.bonkle.whatever.ItemAPI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


/**
 * <h2>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
 * Extends Bukkit ItemStack so it has all preexisting methods,
 * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h2>
 * Most custom functions will return the WhItemStack to allow chaining.<br><br>
 * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
 * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.<br><br>
 * Constructor will take a Material and a quantity (optional) - same as ItemStack.<br>
 * If for any reason you need a true ItemStack, use <code>getItemStack()</code>.
 *
 * @author Cake
 * @see ItemStack
 */
public class WhItemStack extends ItemStack {

    private final Material material;

    /**
     * <h2>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
     * Extends Bukkit ItemStack so it has all preexisting methods,
     * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h2>
     * Most custom functions will return the WhItemStack to allow chaining.<br><br>
     * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
     * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.
     *
     * 
     *
     * @param material Material of the WhItemStack
     * @param amount Quantity items in the of the WhItemStack
     * @see ItemStack
     */
    public WhItemStack(Material material, int amount) { super(material, amount); this.material = material; }

    /**
     * <h2>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
     * Extends Bukkit ItemStack so it has all preexisting methods,
     * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h2>
     * Most custom functions will return the WhItemStack to allow chaining.<br><br>
     * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
     * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.
     *
     * @param material Material of the WhItemStack
     * @param name Name of the item
     * @see ItemStack
     */
    public WhItemStack(Material material, String name) { super(material); this.material = material; setName(name); }

    /**
     * <h2>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
     * Extends Bukkit ItemStack so it has all preexisting methods,
     * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h2>
     * Most custom functions will return the WhItemStack to allow chaining.<br><br>
     * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
     * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.
     *
     * @param material Material of the WhItemStack
     * @see ItemStack
     */
    public WhItemStack(Material material) { super(material); this.material = material; }

    /**
     * <h2>Wrapper function that makes the item unbreakable via the <code>ItemMeta.addEnchant()</code> method.</h2>
     *
     * This makes the item unable to take damage to its durability<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @return WhItemStack with unbreakable set to true
     */
    public WhItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h2>Wrapper function that forcefully sets enchantment via the <code>ItemMeta.addEnchant()</code> method.</h2>
     *
     * This <b>bypasses the Bukkit's enchantment rules</b> and allows you to set any enchantment, i.e. a knock-back enchantment on a stick.<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param enchantment Enchantment to set
     * @param level Level of enchantment to set
     * @return WhItemStack with set enchantment
     */
    public WhItemStack forceAddEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h2>Wrapper function that sets the display name of an item via the <code>ItemMeta.addEnchant()</code> method.</h2>
     *
     * This is a convenience method for setting the display name of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param name Name to set
     * @return WhItemStack  with set name
     */
    public WhItemStack setName(String name) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h2>Wrapper function to set a single line of lore for an item that runs <code>getItemMeta().setLore(List.of(lore))</code>.</h2>
     *
     * <b color="#ff8800;">Warning: Please note that this will overwrite any existing lore!</b><br><br>
     *
     * This is a convenience method for setting the lore of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param lore Lore to set
     * @return WhItemStack with set lore
     */
    public WhItemStack setLore(String lore) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(List.of(lore));
        setItemMeta(itemMeta);
        return this; }


    /**
     * <h2>Wrapper function to set  multiple lines of lore for an item that runs <code>getItemMeta().setLore(List.of(lore))</code>.</h2>
     *
     * <b color="#ff8800;">Warning: Please note that this will overwrite any existing lore!</b><br><br>
     *
     * This is a convenience method for setting the lore of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param lore Lore to set
     * @return WhItemStack with set lore
     */
    public WhItemStack setLore(String... lore) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(List.of(lore));
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h2>Wrapper function to set a specific <b>EXISTING</b> line of lore for an item that runs <code>getItemMeta().setLore(List.of(lore))</code>.</h2>
     *
     * <b color="#ff8800;">Warning: Please note that this will overwrite any existing lore in the line!</b><br><br>
     *
     * This is a convenience method for setting the lore of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param index Index to set
     * @param lore Lore to set
     * @return WhItemStack with set lore
     */
    public WhItemStack setLore(int index, String lore) {
        List<String> loreList = getItemMeta().getLore();
        loreList.set(index, lore);
        getItemMeta().setLore(loreList);
        return this;
    }

    /**
     * <h2>Wrapper function to return an WhItemStack array with length <code>times</code> filled with the WhItemStack</h2>
     *
     * This is a convenience method for repeating an item multiple times in an InventoryMenu.<br><br>
     *
     * <span style="font-style: italic;">Returns an array so you cannot chain any methods after - create the item first THEN repeat it</span>
     *
     * @param times The amount of times to repeat the item
     * @return WhItemStack with set lore
     */
    public WhItemStack[] repeat(int times) {
        WhItemStack[] items = new WhItemStack[times];
        for (int i = 0; i < times; i++) {
            items[i] = this;
        }
        return items;
    }

    /**
     * <h2>Wrapper function to return the lore at <code>pos</code></h2>
     *
     * This is a convenience method to slightly shorten your code.<br><br>
     *
     * @param pos The amount of times to repeat the item
     * @return WhItemStack 's lore at <code>pos</code> or an empty string if no lore
     */
    public String getLore(int pos) {
        try {
            return getItemMeta().getLore().get(pos);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * <h2>Returns a copy of the WhItemStack with the same material, MetaData and amount.</h2>
     *
     * This is done to prevent accidentally modifying the original WhItemStack if you have a public var and want to modiify params.<br><br>
     *
     * <span style="font-style: italic;">Will not interrupt chaining as the return is still a WhItemStack</span>
     *
     * @return Copy of WhItemStack called on
     */
    public WhItemStack clone() {
        return new WhItemStack(material, getAmount())
                .setItemMetaRI(getItemMeta());
    }


    /*public WhItemStack setHeadData(String data) {//Some weird stuff which has been painful to make

        //Log elems (debug)
        HashMap<String, Object> map = (HashMap<String, Object>) this.serialize();
        for (String key : map.keySet()) {
            Debug.log(key + ": " + map.get(key));
        }

        //Set "meta"
        HashMap<Object, Object> meta = new HashMap<>();
        meta.put("==", "ItemMeta");
        meta.put("meta-type", "SKULL");

        HashMap<String, Object> playerProfile = new HashMap<>();
        playerProfile.put("==", "PlayerProfile");
        playerProfile.put("uniqueId", "ca681fa8-fb4b-4ac8-af23-e500c8f755eb");

        ArrayList<HashMap<String, String>> properties = new ArrayList<>();

        HashMap<String, String> propertiesHash = new HashMap<>();
        propertiesHash.put("name", "texture");
        propertiesHash.put("value", data);

        properties.add(propertiesHash);
        playerProfile.put("properties", properties);
        meta.put("skull-owner", playerProfile);

        //Convert the Hashmap back ionto skull meta

        return this;
    }*/

    /**
     * <h2>Returns a copy of the WhItemStack with the same material, MetaData and amount.</h2>
     *
     * @return WhItemStack with the same material, MetaData and amount
     */
    public static WhItemStack fromBukkit(ItemStack itemStack) {
        return new WhItemStack(itemStack.getType(), itemStack.getAmount())
                .setItemMetaRI(itemStack.getItemMeta());
    }

    //RI methods

    /**
     * <h2>Wrapper function to set ItemMeta and allow chaining with other <code>RI</code> methods,
     * runs <code>setItemMeta()</code>.</h2>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param meta ItemMeta to set
     * @return WhItemStack with set meta
     * @see ItemStack#setItemMeta(ItemMeta)
     */
    public WhItemStack setItemMetaRI(ItemMeta meta) {
        setItemMeta(meta);
        return this;
    }

    /**
     * <h2>Wrapper function to set the amount of the items in the ItemStack and allow chaining with other <code>RI</code> methods,
     * runs <code>setAmount()</code>.</h2>
     *
     * <span style="font-style: italic;">Returns the WhItemStack so that you can chain methods</span>
     *
     * @param amount Item amount to set
     * @return WhItemStack with set meta
     * @see ItemStack#setAmount(int)
     */
    public WhItemStack setAmountRI(int amount) {
        setAmount(amount);
        return this;
    }

}
