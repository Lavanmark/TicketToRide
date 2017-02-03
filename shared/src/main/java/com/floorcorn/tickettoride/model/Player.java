package com.floorcorn.tickettoride.model;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Player {

	public enum PlayerColor {
		BLUE, RED, GREEN, YELLOW, BLACK
	};

	private int playerID;
	private int gameID;
	private int userID;
	private PlayerColor color;

	public Player(Player player) {
		this.playerID = player.getPlayerID();
		this.gameID = player.getGameID();
		this.userID = player.getUserID();
		this.color = player.getColor();
	}

	public Player(int userID, int gameID, PlayerColor color) {
		this.userID = userID;
		this.color = color;
		this.gameID = gameID;
		this.playerID = -1;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public int getUserID() {
		return userID;
	}

	public PlayerColor getColor() {
		return color;
	}

	public void setColor(PlayerColor color) {
		this.color = color;
	}

	public boolean isConductor() { return playerID == 0; }
}
