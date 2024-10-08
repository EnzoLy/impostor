package me.enzol.impostor.utilities;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder implements Listener {

  private final ItemStack itemStack;

  public ItemBuilder(Material mat) {
    itemStack = new ItemStack(mat);
  }

  public ItemBuilder(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public ItemBuilder amount(int amount) {
    itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder color(Color color) {
    if (itemStack.getType() == Material.LEATHER_BOOTS
        || itemStack.getType() == Material.LEATHER_CHESTPLATE
        || itemStack.getType() == Material.LEATHER_HELMET
        || itemStack.getType() == Material.LEATHER_LEGGINGS) {
      LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
      meta.setColor(color);
      itemStack.setItemMeta(meta);
      return this;
    } else {
      throw new IllegalArgumentException("color() only applicable for leather armor!");
    }
  }

  public ItemBuilder owner(String owner) {
    SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
    meta.setOwner(owner);
    itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder owner(Player owner) {
    SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
    meta.setOwningPlayer(owner);
    itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder name(String name) {
    ItemMeta meta = itemStack.getItemMeta();
    meta.setDisplayName(CC.translate(name));
    itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder lore(String name) {
    ItemMeta meta = itemStack.getItemMeta();
    List<String> lore = meta.getLore();

    if (lore == null) {
      lore = new ArrayList<>();
    }

    lore.add(ChatColor.translateAlternateColorCodes('&', name));
    meta.setLore(lore);

    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemBuilder lore(String... lore) {
    List<String> toSet = new ArrayList<>();
    ItemMeta meta = itemStack.getItemMeta();

    for (String string : lore) {
      toSet.add(ChatColor.translateAlternateColorCodes('&', string));
    }

    meta.setLore(toSet);
    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemBuilder lore(List<String> lore) {
    List<String> toSet = new ArrayList<>();
    ItemMeta meta = itemStack.getItemMeta();

    for (String string : lore) {
      toSet.add(ChatColor.translateAlternateColorCodes('&', string));
    }

    meta.setLore(toSet);
    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemBuilder durability(int durability) {
    itemStack.setDurability((short) durability);
    return this;
  }

  public ItemBuilder enchantment(Enchantment enchantment, int level) {
    itemStack.addUnsafeEnchantment(enchantment, level);
    return this;
  }

  public ItemBuilder enchantment(Enchantment enchantment) {
    itemStack.addUnsafeEnchantment(enchantment, 1);
    return this;
  }

  public ItemBuilder type(Material material) {
    itemStack.setType(material);
    return this;
  }

  public ItemBuilder clearLore() {
    ItemMeta meta = itemStack.getItemMeta();

    meta.setLore(new ArrayList<>());
    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemBuilder clearEnchantments() {
    for (Enchantment e : itemStack.getEnchantments().keySet()) {
      itemStack.removeEnchantment(e);
    }

    return this;
  }

  public ItemBuilder addItemFlag(ItemFlag itemFlag) {
    ItemMeta meta = itemStack.getItemMeta();
    meta.addItemFlags(itemFlag);
    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemBuilder addItemFlags(ItemFlag... itemFlag) {
    ItemMeta meta = itemStack.getItemMeta();
    meta.addItemFlags(itemFlag);
    itemStack.setItemMeta(meta);

    return this;
  }

  public ItemStack build() {
    addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
    return itemStack;
  }
}
