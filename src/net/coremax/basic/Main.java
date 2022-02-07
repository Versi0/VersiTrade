package net.coremax.basic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.coremax.cmds.user.TradeCmd;
import net.coremax.listeners.TradeListener;
import net.coremax.managers.TradeManager;
import net.coremax.objects.Trade;

public class Main extends JavaPlugin {
	
	public static Main main;
	
	public static Main getPlugin() {
		return main;
	}
	
	public static TradeManager tradeManager;
	
	public void onEnable(){
		main = this;
		getCommand("trade").setExecutor(new TradeCmd());
		Bukkit.getPluginManager().registerEvents(new TradeListener(), this);
		tradeManager = new TradeManager(this);
	}

	public void onDisable(){
		for (Trade trade : tradeManager.getAllTrades()){
			trade.closeTrade();
		}
	}

}