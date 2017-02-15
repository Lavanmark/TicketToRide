package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.model.IGame;

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

    
    private static List<IGame> gamesList = new ArrayList<IGame>();

    private static Map<String, IGame> gamesMap = new HashMap<String, IGame>();

    public GameListContent(List<IGame> games) {
	    GameListContent.gamesList.clear();
        GameListContent.gamesList.addAll(games);
        for(IGame g : games)
            GameListContent.gamesMap.put(String.valueOf(g.getGameID()), g);
    }

    public static void setGamesList(List<IGame> gamesList) {
	    GameListContent.gamesList.clear();
        GameListContent.gamesList.addAll(gamesList);
        for(IGame g : gamesList)
	        GameListContent.gamesMap.put(String.valueOf(g.getGameID()), g);
    }

	public static IGame get(int position) {
		if(position < GameListContent.gamesList.size() && position >= 0)
			return GameListContent.gamesList.get(position);
		return null;
	}

	public static IGame get(String key) {
		if(GameListContent.gamesMap.containsKey(key))
			return GameListContent.gamesMap.get(key);
		return null;
	}

	public static int getSize() {
		return GameListContent.gamesList.size();
	}
}