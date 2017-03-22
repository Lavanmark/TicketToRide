package com.floorcorn.tickettoride.model;

/**
 * Created by Tyler on 2/21/17.
 */

public class PlayerInfo {
	private int playerID = Player.NO_PLAYER_ID;
	private int gameID = Game.NO_GAME_ID;
	private int userID = User.NO_USER_ID;
	private PlayerColor color;
	private String name = null;

	private PlayerInfo(){}

	public PlayerInfo(Player player) {
		this.playerID = player.getPlayerID();
		this.gameID = player.getGameID();
		this.userID = player.getUserID();
		this.color = player.getColor();
		this.name = player.getName();
	}

	public PlayerInfo(int userID, String name, int gameID, PlayerColor color) {
		this.userID = userID;
		this.name = name;
		this.color = color;
		this.gameID = gameID;
		this.playerID = Player.NO_PLAYER_ID;
	}

	public int getPlayerID() {
		return playerID;
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

	public String getName() {
		return name;
	}

	public boolean isConductor() { return playerID == 0; }

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		PlayerInfo player = (PlayerInfo) o;

		if(playerID != player.playerID) return false;
		if(gameID != player.gameID) return false;
		if(userID != player.userID) return false;
		if(color != player.color) return false;
		return name != null ? name.equals(player.name) : player.name == null;

	}

	@Override
	public int hashCode() {
		int result = playerID;
		result = 31 * result + gameID;
		result = 31 * result + userID;
		result = 31 * result + color.hashCode();
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
