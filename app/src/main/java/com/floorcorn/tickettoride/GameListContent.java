package com.floorcorn.tickettoride;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        for (int i = 1; i <= COUNT; i++) {
            addItem(createGameItem(i));
        }
    }

    private static void addItem(GameItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static GameItem createGameItem(int position) {
        return new GameItem(String.valueOf(position), "Game Name " + position, "0/5", makeDetails(position));
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
     * A dummy item representing a piece of content.
     */
    public static class GameItem {
        public final String id;
        public final String content;
        public final String status;
        public final String details;

        public GameItem(String id, String content, String status, String details) {
            this.id = id;
            this.content = content;
            this.status = status;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
