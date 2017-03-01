package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Player {

	private int playerID;
	private int gameID;
	private int userID;
	private PlayerColor color;
	private String name = null;
	private boolean turn = false;

	private Player(){}

	public Player(Player player) {
		this.playerID = player.getPlayerID();
		this.gameID = player.getGameID();
		this.userID = player.getUserID();
		this.color = player.getColor();
		this.name = player.getName();
		this.turn = player.isTurn();
	}

	public Player(int userID, String name, int gameID, PlayerColor color) {
		this.userID = userID;
		this.name = name;
		this.color = color;
		this.gameID = gameID;
		this.playerID = -1;
	}

	@JsonIgnore
	public PlayerInfo getPlayerInfo() {
		return new PlayerInfo(this);
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

	public String getName() {
		return name;
	}

	@JsonIgnore
	public boolean isConductor() { return playerID == 0; }

	public boolean isTurn() {
		return turn;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Player player = (Player) o;

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
