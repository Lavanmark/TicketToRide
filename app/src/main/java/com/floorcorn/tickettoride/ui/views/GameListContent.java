package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.clientModel.Game;

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

    /**
     * An array of sample (dummy) items.
     */
    public static final List<GameItem> ITEMS = new ArrayList<GameItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, GameItem> ITEM_MAP = new HashMap<String, GameItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        Game dummy = new Game("dummyGame", 5);
        addItem(createGameItem(dummy, 1));

    }

    private static void addItem(GameItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static GameItem createGameItem(Game game, int position) {
        return new GameItem(game, String.valueOf(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A Game item containing content about the game
     */
    public static class GameItem {
        public String pos;
        public String id;
        public String name;
        public String status;
        public String details = "details to come";
        public HashMap<String, String> playerMap = new HashMap<>();

        public GameItem(Game game, String pos) {
            this.pos = pos;
            this.id = String.valueOf(game.getGameID());
            this.name = game.getName();
            this.status = "0/0";
            this.playerMap = new HashMap<>();
            String playerName = "player";
            String playerColor = "unknown";
            playerMap.put(playerName, playerColor);
        }

        public void update(Game game){
            //TODO when games are updated
        }
    }
}