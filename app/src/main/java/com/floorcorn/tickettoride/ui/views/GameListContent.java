package com.floorcorn.tickettoride.ui.views;


import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.GameInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GameListContent {

    
    private static List<GameInfo> gamesList = new ArrayList<GameInfo>();

    private static Map<String, GameInfo> gamesMap = new HashMap<String, GameInfo>();

    public GameListContent(List<GameInfo> games) {
	    GameListContent.gamesList.clear();
        GameListContent.gamesList.addAll(games);
        for(GameInfo g : games)
            GameListContent.gamesMap.put(String.valueOf(g.getGameID()), g);
    }

    public static void setGamesList(List<GameInfo> gamesList) {
	    GameListContent.gamesList.clear();
        GameListContent.gamesList.addAll(gamesList);
        for(GameInfo g : gamesList)
	        GameListContent.gamesMap.put(String.valueOf(g.getGameID()), g);
    }

	public static GameInfo get(int position) {
		if(position < GameListContent.gamesList.size() && position >= 0)
			return GameListContent.gamesList.get(position);
		return null;
	}

	public static GameInfo get(String key) {
		if(GameListContent.gamesMap.containsKey(key))
			return GameListContent.gamesMap.get(key);
		return null;
	}

	public static int getSize() {
		return GameListContent.gamesList.size();
	}
}