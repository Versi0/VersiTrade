package net.coremax.cmds.user;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.coremax.basic.Main;
import net.coremax.objects.Trade;
import net.coremax.utils.ChatUtil;

public class TradeCmd implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("akceptuj")) {
				if (Main.tradeManager.getTradeFromAccepter(player) != null) {
					Trade trade = Main.tradeManager.getTradeFromAccepter(player);
					trade.setTradeAccepted(true);
					Main.tradeManager.addTrade(trade);
					trade = null;
				} else {
					ChatUtil.sendMsg(player, "&8>> &cNie masz zadnego zaproszenia do wymiany!");
				}
			} else if (args[0].equalsIgnoreCase("anuluj")) {
				if (Main.tradeManager.getTradeFromAccepter(player) != null) {
					Trade trade = Main.tradeManager.getTradeFromAccepter(player);
					if (!trade.isTradeAccepted()) {
						trade.cancelTrade(true);
					} else {
						ChatUtil.sendMsg(player, "");
						ChatUtil.sendMsg(player, "&8>> &aTransakcja zostala akceptowana!");
						ChatUtil.sendMsg(player, "");
					}
					trade = null;
				} else {
					ChatUtil.sendMsg(player, "");
					ChatUtil.sendMsg(player, "&8>> &cNie masz zadnego zaproszenia do wymiany!");
					ChatUtil.sendMsg(player, "");
				}
			} else if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
				Player accepter = Bukkit.getPlayer(args[0]);
				if (!accepter.getName().equalsIgnoreCase(player.getName())) {
					if (player.getWorld().getName().equalsIgnoreCase(accepter.getWorld().getName())) {
						if ((player.getLocation().distance(accepter.getLocation()) <= 10.0D) || 10.0D < 0.0D) {
							if ((Main.tradeManager.getTradeFromAccepter(player) == null) && (Main.tradeManager.getTradeFromRequester(player) == null)) {
								if ((Main.tradeManager.getTradeFromAccepter(accepter) == null)
										&& (Main.tradeManager.getTradeFromRequester(accepter) == null)) {
									Trade trade2 = new Trade(Main.getPlugin(), player, accepter);
									Main.tradeManager.addTrade(trade2);
									trade2 = null;
								} else {
									ChatUtil.sendMsg(player, "");
									ChatUtil.sendMsg(player, "&8>> &cGracz &7" + accepter.getName() + " &cw tej chwili sie wymienia!");
									ChatUtil.sendMsg(player, "");
								}
							} else {
								ChatUtil.sendMsg(player, "");
								ChatUtil.sendMsg(player, "&8>> &cW tej chwili sie wymieniasz!");
								ChatUtil.sendMsg(player, "");
							}
						} else {
							ChatUtil.sendMsg(player, "");
							ChatUtil.sendMsg(player, "&8>> &cJestes za daleko od gracza, minimalna odleglosc to 10 metrow!");
							ChatUtil.sendMsg(player, "");
						}
					} else {
						ChatUtil.sendMsg(player, "");
						ChatUtil.sendMsg(player, "&8>> &cGracz jest na innym swiecie!");
						ChatUtil.sendMsg(player, "");
					}
				} else {
					ChatUtil.sendMsg(player, "");
					ChatUtil.sendMsg(player, "&8>> &cNie mozesz wymieniac sie z soba!");
					ChatUtil.sendMsg(player, "");
				}
			} else {
				ChatUtil.sendMsg(player, "");
				ChatUtil.sendMsg(player, "&8>> &4Blad: &cNie odnaleziono gracza!");
				ChatUtil.sendMsg(player, "");
			}
		} else {
			ChatUtil.sendMsg(player, "");
			ChatUtil.sendMsg(player, "&8>> &7Dostepne komendy:\n&6/wymiana <nick>\n&6/wymiana akceptuj\n&6/wymiana anuluj");
			ChatUtil.sendMsg(player, "");
		}
		return true;
	}

}
