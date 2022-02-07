package net.coremax.utils;

import org.bukkit.entity.Player;

public class ChatUtil {
	
	public static void sendMsg(Player player, String msg) {
		player.sendMessage(msg.replaceAll("&", "§").replaceAll(">>", "»").replaceAll("<<", "«"));
	}
	
	public static String fixColor(String text) {
		return text.replaceAll("&", "§").replaceAll(">>", "»").replaceAll("<<", "«");
	}

	public static String fix(String text) {
		return text.replaceAll("&", "§").replaceAll(">>", "»").replaceAll("<<", "«");
    }
	
}