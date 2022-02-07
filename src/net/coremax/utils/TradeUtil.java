package net.coremax.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeUtil {
	public static boolean canPlaceItem(Player p, int rawSlot) {
		boolean canPlace = false;
		if (!isTopInventory(p, rawSlot)) {
			canPlace = true;
		}
		if ((isTopInventory(p, rawSlot)) && (((rawSlot >= 0) && (rawSlot < 4)) || ((rawSlot >= 9) && (rawSlot < 13)) || ((rawSlot >= 18) && (rawSlot < 22)) || ((rawSlot >= 27) && (rawSlot < 31)))) {
			canPlace = true;
		}
		return canPlace;
	}

	public static boolean isTopInventory(Player p, int rawSlot) {
		boolean isTop = false;
		if (((p instanceof Player)) && (rawSlot < p.getOpenInventory().getTopInventory().getSize())) {
			isTop = true;
		}
		return isTop;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ItemStack getItem(Material material, int itemAmount, int itemData, String name, List<String> lores) {
		ItemStack item = new ItemStack(material, itemAmount, (short) (byte) itemData);
		if (name != null) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			item.setItemMeta(meta);
		}
		if (lores != null) {
			List<String> lore = new ArrayList();
			for (String l : lores) {
				lore.add(ChatColor.translateAlternateColorCodes('&', l));
			}
			ItemMeta meta2 = item.getItemMeta();
			meta2.setLore(lore);
			item.setItemMeta(meta2);
		}
		return item;
	}

	public static ItemStack getItem(Material material, int itemAmount, int itemData, String name) {
		ItemStack item = new ItemStack(material, itemAmount, (short) (byte) itemData);
		if (name != null) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			item.setItemMeta(meta);
		}
		ItemMeta meta2 = item.getItemMeta();
		item.setItemMeta(meta2);
		return item;
	}
}

