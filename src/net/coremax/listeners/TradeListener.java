package net.coremax.listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.coremax.basic.Main;
import net.coremax.utils.TradeUtil;
import net.coremax.objects.Trade;

public class TradeListener implements Listener {
	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if ((e.getWhoClicked() instanceof Player)) {
			Player p = (Player) e.getWhoClicked();
			Trade trade = null;
			if (Main.tradeManager.getTradeFromAccepter(p) != null) {
				trade = Main.tradeManager.getTradeFromAccepter(p);
			}
			if (Main.tradeManager.getTradeFromRequester(p) != null) {
				trade = Main.tradeManager.getTradeFromRequester(p);
			}
			if ((trade != null) && (trade.hasTradeWindowOpen(p))) {
				for (Iterator localIterator = e.getRawSlots().iterator(); localIterator.hasNext();) {
					int rawSlot = ((Integer) localIterator.next()).intValue();
					if ((TradeUtil.canPlaceItem(p, rawSlot)) && (!trade.isCountdownInProgress())) {
						final Trade finalTrade = trade;
						new BukkitRunnable() {
							public void run() {
								finalTrade.updateOpenTrade();
								Main.tradeManager.addTrade(finalTrade);
							}
						}.runTaskLater(Main.getPlugin(), 1L);
					} else {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (((e.getEntity() instanceof Player)) && (e.getDamage() > 0.0D) && (!e.isCancelled())) {
			final Player p = (Player) e.getEntity();
			final double startHealth = p.getHealthScale();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				public void run() {
					if (p.getHealthScale() != startHealth) {
						Trade trade = null;
						if (Main.tradeManager.getTradeFromAccepter(p) != null) {
							trade = Main.tradeManager.getTradeFromAccepter(p);
						}
						if (Main.tradeManager.getTradeFromRequester(p) != null) {
							trade = Main.tradeManager.getTradeFromRequester(p);
						}
						if (trade != null) {
							trade.cancelTrade(true);
						}
						trade = null;
					}
				}
			}, 0L);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if ((e.getWhoClicked() instanceof Player)) {
			Player p = (Player) e.getWhoClicked();
			Trade trade = null;
			boolean isRequester = false;
			if (Main.tradeManager.getTradeFromAccepter(p) != null) {
				trade = Main.tradeManager.getTradeFromAccepter(p);
				isRequester = false;
			}
			if (Main.tradeManager.getTradeFromRequester(p) != null) {
				trade = Main.tradeManager.getTradeFromRequester(p);
				isRequester = true;
			}
			if ((trade != null) && (trade.hasTradeWindowOpen(p))) {
				if ((e.getClick() == ClickType.LEFT) || (e.getClick() == ClickType.RIGHT)) {
					if ((TradeUtil.canPlaceItem(p, e.getRawSlot())) && (!trade.isCountdownInProgress())) {
						final Trade finalTrade = trade;
						new BukkitRunnable() {
							public void run() {
								finalTrade.updateOpenTrade();
								Main.tradeManager.addTrade(finalTrade);
							}
						}.runTaskLater(Main.getPlugin(), 1L);
					} else {
						if (e.getRawSlot() == 36) {
							if (isRequester) {
								trade.setRequesterReady(true);
								Main.tradeManager.addTrade(trade);
							} else {
								trade.setAccepterReady(true);
								Main.tradeManager.addTrade(trade);
							}
						} else if (e.getRawSlot() == 38) {
							trade.cancelTrade(true);
						}
						e.setCancelled(true);
					}
				} else {
					e.setCancelled(true);
				}
			}
			trade = null;
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if ((e.getPlayer() instanceof Player)) {
			Player p = (Player) e.getPlayer();
			Trade trade = null;
			if (Main.tradeManager.getTradeFromAccepter(p) != null) {
				trade = Main.tradeManager.getTradeFromAccepter(p);
			}
			if (Main.tradeManager.getTradeFromRequester(p) != null) {
				trade = Main.tradeManager.getTradeFromRequester(p);
			}
			if ((trade != null) && (trade.hasTradeWindowOpen(p)) && (!trade.isCancelled())) {
				trade.cancelTrade(true);
			}
			trade = null;
		}
	}

}
