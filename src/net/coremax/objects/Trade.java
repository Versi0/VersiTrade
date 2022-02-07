package net.coremax.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.coremax.basic.Main;
import net.coremax.utils.ChatUtil;
import net.coremax.utils.TradeUtil;

public class Trade {
	private Main plugin;
	private Player tradeRequester;
	private Player tradeAccepter;
	private Inventory requesterInventory;
	private Inventory accepterInventory;
	private ItemStack[] requesterTradeRequestItems;
	private ItemStack[] accepterTradeRequestItems;
	private boolean countdownInProgress;
	private boolean tradeAccepted;
	private boolean requesterReady;
	private boolean accepterReady;
	private boolean tradeReady;
	private boolean cancelled;
	private int countdown;
	public Trade(Main plugin, Player tradeRequester, Player tradeAccepter) {
		this.tradeRequester = null;
		this.tradeAccepter = null;
		this.requesterInventory = null;
		this.accepterInventory = null;
		this.requesterTradeRequestItems = new ItemStack[0];
		this.accepterTradeRequestItems = new ItemStack[0];
		this.countdownInProgress = false;
		this.tradeAccepted = false;
		this.requesterReady = false;
		this.accepterReady = false;
		this.tradeReady = false;
		this.cancelled = false;
		this.countdown = -1;
		this.plugin = plugin;
		this.tradeRequester = tradeRequester;
		this.tradeAccepter = tradeAccepter;
		ChatUtil.sendMsg(tradeRequester, "&8>> &aWymiana wyslana do gracza &7" + tradeAccepter.getName());
		ChatUtil.sendMsg(tradeAccepter, "&8>> &aGracz &7" + tradeRequester.getName() + " &azaprosil cie do wymiany, masz 15 sekund by akceptowac &7/wymiana akceptuj&a.");
		startTimeOutCounter();
	}

	public void openTrade() {
		if (!isCancelled()) {
			if ((this.tradeRequester.isOnline()) && (this.tradeAccepter.isOnline())
					&& ((this.tradeRequester.getLocation().distance(this.tradeAccepter.getLocation()) <= 10.0D || 10.0D < 0.0D))) {
				String invName = ChatUtil.fixColor("&8>>&aTy                  &8>>&cOn");
				Inventory inv = Bukkit.createInventory(null, 45, invName);
				ItemStack divider = TradeUtil.getItem(Material.IRON_FENCE, 1, 0, " ");
				ItemStack declineTrade = TradeUtil.getItem(Material.MAGMA_CREAM, 1, 0, ChatUtil.fixColor("&cKliknij, aby anulowac!"));
				ItemStack acceptTrade = TradeUtil.getItem(Material.SLIME_BALL, 1, 0, ChatUtil.fixColor("&aKliknij, aby akceptowac!"));
				ItemStack notReady = TradeUtil.getItem(Material.INK_SACK, 1, 8, ChatUtil.fixColor("&cNie gotowy"));
				inv.setItem(4, divider);
				inv.setItem(13, divider);
				inv.setItem(22, divider);
				inv.setItem(31, divider);
				inv.setItem(40, divider);
				inv.setItem(36, acceptTrade);
				inv.setItem(37, declineTrade);
				inv.setItem(39, notReady);
				inv.setItem(44, notReady);
				(this.requesterInventory = Bukkit.createInventory(null, 45, invName)).setContents(inv.getContents());
				(this.accepterInventory = Bukkit.createInventory(null, 45, invName)).setContents(inv.getContents());
				this.tradeRequester.openInventory(this.requesterInventory);
				this.tradeAccepter.openInventory(this.accepterInventory);
			} else {
				cancelTrade(true);
			}
		}
	}

	public void closeTrade() {
		if ((this.tradeRequester.isOnline()) && (this.tradeRequester.getOpenInventory() != null) && (this.tradeRequester.getOpenInventory().getTopInventory() != null)) {
			this.tradeRequester.closeInventory();
			this.tradeRequester.updateInventory();
		}
		if ((this.tradeAccepter.isOnline()) && (this.tradeAccepter.getOpenInventory() != null) && (this.tradeAccepter.getOpenInventory().getTopInventory() != null)) {
			this.tradeAccepter.closeInventory();
			this.tradeAccepter.updateInventory();
		}
	}

	public void startTimeOutCounter() {
		new BukkitRunnable() {
			int seconds = 0;

			public void run() {
				if (!Trade.this.isCancelled()) {
					if (this.seconds < 15) {
						if ((!Trade.this.tradeRequester.isOnline()) || (!Trade.this.tradeAccepter.isOnline())
								|| (!Trade.this.tradeRequester.getWorld().getName().equalsIgnoreCase(Trade.this.tradeAccepter.getWorld().getName()))
								|| ((Trade.this.tradeRequester.getLocation().distance(Trade.this.tradeAccepter.getLocation()) > 10.0D && 10.0D >= 0.0D))) {
							Trade.this.cancelTrade(true);
							cancel();
						}
						if (Trade.this.isTradeAccepted()) {
							cancel();
						}
					} else {
						Trade.this.cancelTrade(true);
						cancel();
					}
					this.seconds += 1;
				} else {
					cancel();
				}
			}
		}.runTaskTimer(this.plugin, 20L, 20L);
	}

	public void startReadyCounter() {
		this.countdownInProgress = true;
		this.countdown = 5;
		new BukkitRunnable() {
			int seconds = 0;

			public void run() {
				if (!Trade.this.isCancelled()) {
					Trade.this.updateOpenTrade();
					if (this.seconds < 5) {
						if ((!Trade.this.tradeRequester.isOnline()) || (!Trade.this.tradeAccepter.isOnline())
								|| (!Trade.this.hasTradeWindowOpen(Trade.this.tradeRequester))
								|| (!Trade.this.hasTradeWindowOpen(Trade.this.tradeAccepter))) {
							Trade.this.cancelTrade(true);
							cancel();
						}
					} else {
						Trade.this.cancelTrade(false);
						cancel();
					}
					Trade this$0 = Trade.this;
					this$0.countdown -= 1;
					this.seconds += 1;
				} else {
					cancel();
				}
			}
		}.runTaskTimer(this.plugin, 20L, 20L);
	}

	public boolean isCountdownInProgress() {
		return this.countdownInProgress;
	}

	public void giveItemsFromTrade() {
		ItemStack[] empty = new ItemStack[0];
		this.tradeRequester.getInventory().addItem(getAccepterTradeRequestItems());
		setAccepterTradeRequestItems(empty);
		this.tradeAccepter.getInventory().addItem(getRequesterTradeRequestItems());
		setRequesterTradeRequestItems(empty);
	}

	public void returnItems() {
		ItemStack[] empty = new ItemStack[0];
		this.tradeRequester.getInventory().addItem(getRequesterTradeRequestItems());
		setRequesterTradeRequestItems(empty);
		this.tradeAccepter.getInventory().addItem(getAccepterTradeRequestItems());
		setAccepterTradeRequestItems(empty);
	}

	public boolean hasTradeWindowOpen(Player p) {
		boolean open = false;
		if (p.getOpenInventory() != null) {
			Inventory inv = p.getOpenInventory().getTopInventory();
			if ((inv.getSize() == 45) && (inv.getTitle().contains(ChatUtil.fixColor("&8>>&aTy                  &8>>&cOn")))) {
				open = true;
			}
		}
		return open;
	}

	public void updateOpenTrade() {
		if (!isCancelled()) {
			if ((this.tradeRequester.isOnline()) && (this.tradeAccepter.isOnline()) && ((this.tradeRequester.getLocation().distance(this.tradeAccepter.getLocation()) <= 10.0D || 10.0D < 0.0D))) {
				setRequesterTradeRequestItems(Main.tradeManager.getItemsRequester(getRequester()));
				setAccepterTradeRequestItems(Main.tradeManager.getItemsAccepter(getAccepter()));
				String invName = ChatUtil.fixColor("&8>>&aTy                  &8>>&cOn");
				Inventory inv = Bukkit.createInventory(null, 45, invName);
				int dividerCountdown = 1;
				if (this.countdown > 0) {
					dividerCountdown = this.countdown;
				}
				ItemStack divider = TradeUtil.getItem(Material.IRON_FENCE, dividerCountdown, 0, " ", null);
				ItemStack declineTrade = TradeUtil.getItem(Material.MAGMA_CREAM, 1, 0, ChatUtil.fixColor("&cKliknij, aby anulowac!"));
				ItemStack acceptTrade = TradeUtil.getItem(Material.SLIME_BALL, 1, 0, ChatUtil.fixColor("&aKliknij, aby akceptowac!"));
				ItemStack ready = TradeUtil.getItem(Material.INK_SACK, 1, 10, ChatUtil.fixColor("&aGotowy"));
				ItemStack notReady = TradeUtil.getItem(Material.INK_SACK, 1, 8, ChatUtil.fixColor("&cNie gotowy"));
				inv.setItem(4, divider);
				inv.setItem(13, divider);
				inv.setItem(22, divider);
				inv.setItem(31, divider);
				inv.setItem(40, divider);
				inv.setItem(36, acceptTrade);
				inv.setItem(37, declineTrade);
				inv.setItem(39, notReady);
				inv.setItem(44, notReady);
				Inventory requesterInventory = Bukkit.createInventory(null, 45, invName);
				requesterInventory.setContents(inv.getContents());
				requesterInventory = Main.tradeManager.setItemsLeft(requesterInventory, getRequesterTradeRequestItems());
				requesterInventory = Main.tradeManager.setItemsRight(requesterInventory, getAccepterTradeRequestItems());
				Inventory accepterInventory = Bukkit.createInventory(null, 45, invName);
				accepterInventory.setContents(inv.getContents());
				accepterInventory = Main.tradeManager.setItemsLeft(accepterInventory, getAccepterTradeRequestItems());
				accepterInventory = Main.tradeManager.setItemsRight(accepterInventory, getRequesterTradeRequestItems());
				if (isRequesterReady()) {
					requesterInventory.setItem(39, ready);
					accepterInventory.setItem(44, ready);
				}
				if (isAccepterReady()) {
					requesterInventory.setItem(44, ready);
					accepterInventory.setItem(39, ready);
				}
				this.tradeRequester.getOpenInventory().getTopInventory().setContents(requesterInventory.getContents());
				this.tradeAccepter.getOpenInventory().getTopInventory().setContents(accepterInventory.getContents());
			} else {
				cancelTrade(true);
			}
		}
	}

	public void setRequesterTradeRequestItems(ItemStack[] items) {
		this.requesterTradeRequestItems = items;
	}

	public ItemStack[] getRequesterTradeRequestItems() {
		return this.requesterTradeRequestItems;
	}

	public void setAccepterTradeRequestItems(ItemStack[] items) {
		this.accepterTradeRequestItems = items;
	}

	public ItemStack[] getAccepterTradeRequestItems() {
		return this.accepterTradeRequestItems;
	}

	public void setTradeAccepted(boolean bool) {
		ChatUtil.sendMsg(tradeRequester, "&8>> &aWymiana zostala akceptowana!");
		ChatUtil.sendMsg(tradeAccepter, "&8>> &aWymiana zostala akceptowana!");
		openTrade();
		this.tradeAccepted = bool;
	}

	public boolean isTradeAccepted() {
		return this.tradeAccepted;
	}

	public void setRequesterReady(boolean bool) {
		this.requesterReady = bool;
		updateOpenTrade();
		if ((isRequesterReady()) && (isAccepterReady()) && (!isCountdownInProgress())) {
			startReadyCounter();
		}
	}

	public boolean isRequesterReady() {
		return this.requesterReady;
	}

	public void setAccepterReady(boolean bool) {
		this.accepterReady = bool;
		updateOpenTrade();
		if ((isRequesterReady()) && (isAccepterReady()) && (!isCountdownInProgress())) {
			startReadyCounter();
		}
	}

	public boolean isAccepterReady() {
		return this.accepterReady;
	}

	public void setTradeReady(boolean bool) {
		this.tradeReady = bool;
	}

	public boolean isTradeReady() {
		return this.tradeReady;
	}

	public void cancelTrade(boolean cancelled) {
		this.cancelled = true;
		if (cancelled) {
			returnItems();
			closeTrade();
			if ((this.tradeRequester.getLocation().distance(this.tradeAccepter.getLocation()) > 10.0D && 10.0D >= 0.0D)) {
				if (this.tradeRequester.isOnline()) {
					ChatUtil.sendMsg(tradeRequester, "&8>> &cJestes za daleko od gracza, minimalna odleglosc to 10 metrow!");
				}
				if (this.tradeAccepter.isOnline()) {
					ChatUtil.sendMsg(tradeAccepter, "&8>> &cJestes za daleko od gracza, minimalna odleglosc to 10 metrow!");
				}
			}
			if (this.tradeRequester.isOnline()) {
				ChatUtil.sendMsg(tradeRequester, "&8>> &cWymiana zostala anulowana!");
			}
			if (this.tradeAccepter.isOnline()) {
				ChatUtil.sendMsg(tradeAccepter, "&8>> &cWymiana zostala anulowana!");
			}
		} else {
			giveItemsFromTrade();
			closeTrade();
			if (this.tradeRequester.isOnline()) {
				ChatUtil.sendMsg(tradeRequester, "&8>> &aWymiana zostala zakonczona sukcesem!");
			}
			if (this.tradeAccepter.isOnline()) {
				ChatUtil.sendMsg(tradeAccepter, "&8>> &aWymiana zostala zakonczona sukcesem!");
			}
		}
		this.countdownInProgress = false;
		Main.tradeManager.removeTrade(this);
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public Player getRequester() {
		return this.tradeRequester;
	}

	public Player getAccepter() {
		return this.tradeAccepter;
	}
}

