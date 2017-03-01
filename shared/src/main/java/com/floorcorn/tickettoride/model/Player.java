package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Player {

	//info
	private int playerID = -1;
	private int gameID = -1;
	private int userID = -1;
	private PlayerColor color = null;
	private String name = null;

	//non info
	private boolean turn = false;
	private int score = 0;
	private int trainCarsLeft = 0;
	private int totalTrainCards = 0;
	private int totalDestinationCards = 0;
	private List<DestinationCard> destinationCards = null;
	private Map<TrainCardColor, Integer> trainCards = null;
	private List<Route> routesClaimed = null;

	// for JACKSON
	private Player(){}

	public Player(Player player) {
		this.playerID = player.getPlayerID();
		this.gameID = player.getGameID();
		this.userID = player.getUserID();
		this.color = player.getColor();
		this.name = player.getName();
		this.turn = player.isTurn();
		this.score = player.getScore();
		this.trainCarsLeft = player.getTrainCarsLeft();
		this.totalTrainCards = player.getTotalTrainCards();
		this.totalDestinationCards = player.getTotalDestinationCards();
		this.destinationCards = player.getDestinationCards();
		this.trainCards = player.getTrainCards();
		this.routesClaimed = player.getRoutesClaimed();
	}

	public Player(int userID, String name, int gameID, PlayerColor color) {
		this.userID = userID;
		this.name = name;
		this.color = color;
		this.gameID = gameID;
		this.playerID = -1;
		this.turn = false;
		this.score = 0;
		this.trainCarsLeft = 45;
		this.totalTrainCards = 0;
		this.totalDestinationCards = 0;
		this.destinationCards = new ArrayList<>();
		this.trainCards = new HashMap<>();
		this.routesClaimed = new ArrayList<>();
	}

	@JsonIgnore
	public PlayerInfo getPlayerInfo() {
		return new PlayerInfo(this);
	}

	@JsonIgnore
	public Player getCensoredPlayer(User user) {
		if(user.getUserID() == userID)
			return this;
		Player p = new Player(this);
		p.setDestinationCards(new ArrayList<DestinationCard>());
		p.setTrainCards(new HashMap<TrainCardColor, Integer>());
		return p;
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
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	//TODO update these getters and setters as needed:

	//==============================================
	//
	// GETTERS AND SETTERS MIGHT NOT BE WHAT WE WANT
	//
	//==============================================

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTrainCarsLeft() {
		return trainCarsLeft;
	}

	public void setTrainCarsLeft(int trainCarsLeft) {
		this.trainCarsLeft = trainCarsLeft;
	}

	public int getTotalTrainCards() {
		return totalTrainCards;
	}

	public void setTotalTrainCards(int totalTrainCards) {
		this.totalTrainCards = totalTrainCards;
	}

	public int getTotalDestinationCards() {
		return totalDestinationCards;
	}

	public void setTotalDestinationCards(int totalDestinationCards) {
		this.totalDestinationCards = totalDestinationCards;
	}

	public List<DestinationCard> getDestinationCards() {
		return destinationCards;
	}

	public void setDestinationCards(List<DestinationCard> destinationCards) {
		this.destinationCards = destinationCards;
	}

	public Map<TrainCardColor, Integer> getTrainCards() {
		return trainCards;
	}

	public void setTrainCards(Map<TrainCardColor, Integer> trainCards) {
		this.trainCards = trainCards;
	}

	public List<Route> getRoutesClaimed() {
		return routesClaimed;
	}

	public void setRoutesClaimed(List<Route> routesClaimed) {
		this.routesClaimed = routesClaimed;
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
