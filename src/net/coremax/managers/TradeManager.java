package net.coremax.managers;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.coremax.basic.Main;
import net.coremax.objects.Trade;

public class TradeManager {
	public Main plugin;
	private ArrayList<Trade> tradeList;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TradeManager(Main instance) {
		this.tradeList = new ArrayList();
		this.plugin = instance;
	}

	public ArrayList<Trade> getAllTrades() {
		return this.tradeList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeTrade(Trade trade) {
		ArrayList<Trade> trades = new ArrayList();
		for (Trade t : this.tradeList) {
			if ((!t.getRequester().getName().equalsIgnoreCase(trade.getRequester().getName())) || (!t.getAccepter().getName().equalsIgnoreCase(trade.getAccepter().getName()))) {
				trades.add(t);
			}
		}
		this.tradeList = trades;
	}

	public void addTrade(Trade trade) {
		removeTrade(trade);
		this.tradeList.add(trade);
	}

	public Trade getTradeFromRequester(Player requester) {
		Trade trade = null;
		for (Trade t : this.tradeList) {
			if (t.getRequester().getName().equalsIgnoreCase(requester.getName())) {
				trade = t;
			}
		}
		return trade;
	}

	public Trade getTradeFromAccepter(Player accepter) {
		Trade trade = null;
		for (Trade t : this.tradeList) {
			if (t.getAccepter().getName().equalsIgnoreCase(accepter.getName())) {
				trade = t;
			}
		}
		return trade;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Inventory setItemsLeft(Inventory inventory, ItemStack[] itemsArray) {
		ArrayList<ItemStack> items = new ArrayList();
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = itemsArray).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			items.add(item);
		}
		int slot = 0;
		while (items.size() > 0) {
			if ((slot != 3) && (slot != 12) && (slot != 21) && (slot != 30)) {
				inventory.setItem(slot, (ItemStack) items.get(0));
				items.remove(items.get(0));
				slot++;
			} else {
				inventory.setItem(slot, (ItemStack) items.get(0));
				items.remove(items.get(0));
				slot += 6;
			}
		}
		return inventory;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Inventory setItemsRight(Inventory inventory, ItemStack[] itemsArray) {
		ArrayList<ItemStack> items = new ArrayList();
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = itemsArray).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			items.add(item);
		}
		int slot = 5;
		while (items.size() > 0) {
			if ((slot != 8) && (slot != 17) && (slot != 26) && (slot != 35)) {
				inventory.setItem(slot, (ItemStack) items.get(0));
				items.remove(items.get(0));
				slot++;
			} else {
				inventory.setItem(slot, (ItemStack) items.get(0));
				items.remove(items.get(0));
				slot += 6;
			}
		}
		return inventory;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ItemStack[] getItemsRequester(Player p) {
		ArrayList<ItemStack> items = new ArrayList();
		if (getTradeFromRequester(p) != null) {
			Trade trade = getTradeFromRequester(p);
			if (trade.hasTradeWindowOpen(p)) {
				Inventory inv = p.getOpenInventory().getTopInventory();
				for (int slot = 0; slot < 4; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 9; slot < 13; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 18; slot < 22; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 27; slot < 31; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
			}
		}
		return (ItemStack[]) items.toArray(new ItemStack[items.size()]);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ItemStack[] getItemsAccepter(Player p) {
		ArrayList<ItemStack> items = new ArrayList();
		if (getTradeFromAccepter(p) != null) {
			Trade trade = getTradeFromAccepter(p);
			if (trade.hasTradeWindowOpen(p)) {
				Inventory inv = p.getOpenInventory().getTopInventory();
				for (int slot = 0; slot < 4; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 9; slot < 13; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 18; slot < 22; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
				for (int slot = 27; slot < 31; slot++) {
					if (inv.getItem(slot) != null) {
						items.add(inv.getItem(slot));
					}
				}
			}
		}
		return (ItemStack[]) items.toArray(new ItemStack[items.size()]);
	}
}
