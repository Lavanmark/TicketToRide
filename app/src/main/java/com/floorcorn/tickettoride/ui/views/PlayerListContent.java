package com.floorcorn.tickettoride.ui.views;


import com.floorcorn.tickettoride.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 2/15/17.
 */

public class PlayerListContent {
	private static List<Player> playersList = new ArrayList<Player>();

	private static Map<String, Player> playersMap = new HashMap<String, Player>();

	public PlayerListContent(List<Player> players) {
		PlayerListContent.playersList.clear();
		PlayerListContent.playersList.addAll(players);
		for(Player p : players)
			PlayerListContent.playersMap.put(String.valueOf(p.getPlayerID()), p);
	}

	public static void setPlayerList(List<Player> players) {
		PlayerListContent.playersList.clear();
		PlayerListContent.playersList.addAll(players);
		for(Player p : players)
			PlayerListContent.playersMap.put(String.valueOf(p.getPlayerID()), p);
	}

	public static Player get(int position) {
		if(position < PlayerListContent.playersList.size() && position >= 0)
			return PlayerListContent.playersList.get(position);
		return null;
	}

	public static Player get(String key) {
		if(PlayerListContent.playersMap.containsKey(key))
			return PlayerListContent.playersMap.get(key);
		return null;
	}

	public static int getSize() {
		return PlayerListContent.playersList.size();
	}
}
