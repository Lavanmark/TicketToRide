package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.log.Corn;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Player {

	public static final int NO_PLAYER_ID = -1;
	//info
	private int playerID = NO_PLAYER_ID;
	private int gameID = Game.NO_GAME_ID;
	private int userID = User.NO_USER_ID;
	private PlayerColor color = null;
	private String name = null;

	//non info
	private boolean turn = false;
	private int score = 0;
	private int trainCarsLeft = Game.INITIAL_TRAIN_CARS;
	private int totalTrainCards = 0;
	private int totalDestinationCards = 0;
	private List<DestinationCard> destinationCards = null;
	private Map<TrainCardColor, Integer> trainCards = null;
	private List<Route> routesClaimed = null;
	private int longestRoute = 0;

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
		this.longestRoute = player.getLongestRoute();
		this.destinationCards = new ArrayList<>(player.getDestinationCards());
		this.trainCards = new EnumMap<>(player.getTrainCards());
		this.routesClaimed = new ArrayList<>(player.getRoutesClaimed());
	}

	public Player(int userID, String name, int gameID, PlayerColor color) {
		this.userID = userID;
		this.name = name;
		this.color = color;
		this.gameID = gameID;
		this.playerID = NO_PLAYER_ID;
		this.turn = false;
		this.score = 0;
		this.trainCarsLeft = Game.INITIAL_TRAIN_CARS;
		this.totalTrainCards = 0;
		this.totalDestinationCards = 0;
		this.longestRoute = 0;
		this.destinationCards = new ArrayList<>();
		this.trainCards = new EnumMap<>(TrainCardColor.class);
		this.routesClaimed = new ArrayList<>();
	}

	public PlayerInfo getPlayerInfo() {
		return new PlayerInfo(this);
	}

	public Player getCensoredPlayer(User user) {
		if(user.getUserID() == userID)
			return new Player(this);
		Player p = new Player(this);
		p.setDestinationCards(new ArrayList<DestinationCard>());
		p.setTrainCards(new EnumMap<TrainCardColor, Integer>(TrainCardColor.class));
		return p;
	}

	public boolean isCensoredPlayer() {
		if(totalDestinationCards != destinationCards.size())
			return true;
		if(totalTrainCards > 0 && trainCards.size() == 0)
			return true;
		return false;
	}

	void addToScore(int amt) {
		score += amt;
	}
	
	public int getPlayerID() {
		return playerID;
	}

	void setPlayerID(int playerID) {
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

	public int getTrainCarsLeft() {
		return trainCarsLeft;
	}

	public int getTotalTrainCards() {
		return totalTrainCards;
	}

	public int getTotalDestinationCards() {
		return totalDestinationCards;
	}

	public List<DestinationCard> getDestinationCards() {
		return destinationCards;
	}

	public DestinationCard[] getDiscardableDestinationCards() {
		int discardable = 0;
		for(DestinationCard dc : destinationCards)
			if(dc.canDiscard())
				discardable++;

		if(discardable == 0)
			return null;

		DestinationCard[] cardArray = new DestinationCard[discardable];
		int i = 0;
		for(DestinationCard dc : destinationCards)
			if(dc.canDiscard())
				cardArray[i++] = dc;
		return cardArray;
	}

	private void setDestinationCards(List<DestinationCard> destinationCards) {
		this.destinationCards = new ArrayList<>(destinationCards);
	}

	public Map<TrainCardColor, Integer> getTrainCards() {
		return trainCards;
	}

	private void setTrainCards(Map<TrainCardColor, Integer> trainCards) {
		this.trainCards = new EnumMap<>(trainCards);
	}

	public List<Route> getRoutesClaimed() {
		return routesClaimed;
	}


	public void claimRoute(Route route){
		routesClaimed.add(route);
		score += route.getValue();
		System.out.println("checking destinations.");
		checkDestinations();
	}
	
	private void checkDestinations() {
		for(DestinationCard dest : destinationCards) {
			if(!dest.isComplete()) //TODO this is causing them not to be checked client side
				if(dest.checkCompletion(routesClaimed))
					addToScore(dest.getValue());
		}
	}

	public int calcualteLongestRoute(){
		Map<City, List<Route>> map = Route.buildCityRouteMap(routesClaimed);
		int longestPathTemp = 0;
		for(City city : map.keySet()) {
			int cur = longest(map, city, 0);
			if(cur > longestPathTemp)
				longestPathTemp = cur;
		}
		
		longestRoute = longestPathTemp;
		return longestRoute;
	}
	
	
	private int longest(Map<City, List<Route>> map, City city, int best) {
		int here = best;
		for(Route route : map.get(city)) {
			if(route.visited)
				continue;
			route.visited = true;
			int next = longest(map, city.equals(route.getFirstCity())? route.getSecondCity() : route.getFirstCity(), here + route.getLength());
			route.visited = false;
			if(next > best)
				best = next;
		}
		return best;
	}

	public int getLongestRoute(){ // just a simple getter
		return longestRoute;
	}

	public boolean removeTrainCars(int amount){
		if(trainCarsLeft >= amount) {
			trainCarsLeft -= amount;
			return true;
		} else {
			return false;
		}
	}

	public boolean addDestinationCard(DestinationCard card){
		if(destinationCards == null)
			return false;
		if(destinationCards.contains(card))
			return false;
		if(!destinationCards.add(card))
			return false;
		totalDestinationCards++;
		return true;
	}

	public boolean addTrainCard(TrainCard card){
		if(card == null)
			return false;
		int cur = 0;
		if(trainCards.containsKey(card.getColor()))
			cur = trainCards.get(card.getColor());
		trainCards.put(card.getColor(), cur + 1);
		totalTrainCards++;
		return true;
	}

	public boolean removeDestinationCard(DestinationCard card){
		if(destinationCards.remove(card)) {
			totalDestinationCards--;
			return true;
		}
		return false;
	}

	public void markAllNotDiscardable() {
		for(DestinationCard dc : destinationCards)
			dc.setCanDiscard(false);
	}

	public boolean removeTrainCard(TrainCard card){
		System.out.println("card color " +card.getColor().toString());
		if(!trainCards.containsKey(card.getColor()))
			return false;
		if(trainCards.get(card.getColor()) > 0) {
			trainCards.put(card.getColor(), trainCards.get(card.getColor()) - 1);
			totalTrainCards--;
			System.out.println("removed card " + card.getColor().toString());
			return true;
		} else {
			Corn.log("Player is out of cards!");
			return false;
		}
	}

	public String getCriticalPlayerInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(name).append("\n");
		sb.append("Color: ").append(color).append("\n");
		sb.append("Score: ").append(score).append("\n");
		sb.append("Train Cards: ").append(totalTrainCards).append("\n");
		sb.append("Destination Cards: ").append(totalDestinationCards).append("\n");
		sb.append("Train Cars: ").append(trainCarsLeft).append("\n");
		sb.append("Longest Path: ").append(longestRoute);

		return sb.toString();
	}

	protected void update(Player player) {
		this.turn = player.isTurn();
		this.score = player.getScore();
		this.trainCarsLeft = player.getTrainCarsLeft();
		this.totalTrainCards = player.getTotalTrainCards();
		this.totalDestinationCards = player.getTotalDestinationCards();
		this.destinationCards = new ArrayList<>(player.getDestinationCards());
		this.trainCards = new EnumMap<>(player.getTrainCards());
		this.routesClaimed = new ArrayList<>(player.getRoutesClaimed());
		this.longestRoute = player.getLongestRoute();
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
