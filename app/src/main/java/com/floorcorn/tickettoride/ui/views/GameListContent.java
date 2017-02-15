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
        gamesList.addAll(games);
        for(IGame g : games)
            gamesMap.put(String.valueOf(g.getGameID()), g);
    }

    public static void setGamesList(List<IGame> gamesList) {
	    GameListContent.gamesList.clear();
        GameListContent.gamesList.addAll(gamesList);
        for(IGame g : gamesList)
            gamesMap.put(String.valueOf(g.getGameID()), g);
    }

	public static IGame get(int position) {
		if(position < gamesList.size() && position >= 0)
			return gamesList.get(position);
		return null;
	}

	public static IGame get(String key) {
		if(gamesMap.containsKey(key))
			return gamesMap.get(key);
		return null;
	}

	public static int getSize() {
		return gamesList.size();
	}
}