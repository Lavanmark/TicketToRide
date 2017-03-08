package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.City;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.ui.views.IView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

/**
 * @author Created by Tyler on 2/4/2017.
 *
 * @author Joseph Hansen
 */

public class UIFacade {

    // Instance variables and other class-specific things.

    private ClientModel clientModelRoot;
    private ServerProxy serverProxy;
    private Poller poller;

    // TODO: add all the types of sorting we may want
    public enum GameSortStyle { ASC_GAMEID, DESC_GAMEID };

    // Things relating to private constructor and singleton pattern.

    private UIFacade() {
        clientModelRoot = new ClientModel();
        serverProxy = new ServerProxy();
        serverProxy.setPort("8080");
        serverProxy.setHost("10.24.215.82");

        poller = new Poller(serverProxy, clientModelRoot);
    }
    private static UIFacade instance = null;
    public static UIFacade getInstance() {
        if (instance == null)
            instance = new UIFacade();
        return instance;
    }

    // Login and register related.

    /**
     * Prepares a User object for parameter to login(...) in ServerProxy. Calls login(...).
     * Throws a BadUserException if server side cannot authenticate given the parameters.
     * @param username String username
     * @param password String password
     */
    public void login(String username, String password) throws BadUserException {
        User user = new User(username, password);
        user = serverProxy.login(user);
	    clientModelRoot.setCurrentUser(user);
        requestGames();
    }

    /**
     * Prepares a User object for parameter to register(...) in ServerProxy. Calls register(...).
     * Throws UserCreationException if server side cannot register the user with the given
     * parameters.
     * @param username String username
     * @param password String password
     * @param firstname String firstname
     * @param lastname String lastname
     */
    public void register(String username, String password, String firstname, String lastname) throws UserCreationException {
        User user = new User(username, password, firstname + " " + lastname);
        user = serverProxy.register(user);
	    clientModelRoot.setCurrentUser(user);
        try {
            requestGames();
        } catch(BadUserException e) {
            e.printStackTrace();
        }
    }

    // User and game related.

    /**
     * Gets the current user from the ClientModel.
     * @return User object from ClientModel
     */
    public User getUser() {
        return clientModelRoot.getCurrentUser();
    }

    /**
     * Returns the games from the ClientModel.
     * @return Set of Game objects from the ClientModel
     */
    public Set<GameInfo> getGames() {
        return clientModelRoot.getGames();
    }

    /**
     * Returns the games that the user has joined.
     * @param user User object
     * @return Set of Game objects from the ClientModel
     */
    public Set<GameInfo> getGames(User user) {
	    user = new User(user);
        return clientModelRoot.getGames(user);
    }

    /**
     * Sorts some games according to the sort style.
     * @param games Set of Game objects to sort
     * @param sortStyle GameSortStyle enum variable designates how to sort
     * @return Same Game objects, but sorted
     */
    private List<GameInfo> sortGames(Set<GameInfo> games, GameSortStyle sortStyle) {
        // TODO:
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the games from ClientModel and sorts them before returning. See GameSortStyle enum.
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of Game objects from the ClientModel, sorted
     */
    public List<GameInfo> getGames(GameSortStyle sortStyle) {
        Set<GameInfo> gamesSet = getGames();
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Gets the games from the ClientModel that the user has joined. Sorts them before
     * returning. See GameSortStyle enum.
     * @param user User object
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of Game objects from the ClientModel, sorted
     */
    public List<GameInfo> getGames(User user, GameSortStyle sortStyle) {
	    user = new User(user);
        Set<GameInfo> gamesSet = getGames(user);
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Returns Game that matches the gameID parameter. Will return null if games returned from
     * ClientModel is null or if gameID not found in games.
     * @param gameID int ID
     * @return Game matching gameID from ClientModel games; can be null
     */
    public GameInfo getGameInfo(int gameID) {
        Set<GameInfo> games = getGames();
        if (games == null) return null;
        for (GameInfo gi : games) {
            if (gi.getGameID() == gameID) {
                return gi;
            }
        }
        return null;
    }

    /**
     * Returns the Game from ClientModel.getCurrentGame().
     * @return Game object
     */
    public Game getCurrentGame() {
        return clientModelRoot.getCurrentGame();
    }

    /**
     * Requests current game from server. Re-throws a BadUserException if authentication doesn't
     * work right on server side.
     * @param game GameInfo object
     * @throws BadUserException
     */
	public void requestGame(GameInfo game) throws BadUserException {
		Game cgame = serverProxy.getGame(clientModelRoot.getCurrentUser(), game.getGameID());
		//System.out.println("still gonna do it");
        clientModelRoot.setCurrentGame(cgame);
	}

    /**
     * Gets games from the ServerProxy and updates the games in the ClientModel. Re-throws
     * BadUserException if authentication doesn't work on the server side.
     */
    public void requestGames() throws BadUserException {
        clientModelRoot.setGames(serverProxy.getGames(getUser()));
	    //for(GameInfo gi : clientModelRoot.getGames())
		//    System.out.println(gi.getGameID() + " " + gi.getName());
    }

    /**
     * The current user leaves the game that matches gameID. Throws
     * BadUserException if user cannot be authenticated by server. Throws GameActionException if
     * user can't leave the game (not in the game, etc).
     * @param gameID int game ID
     */
    public boolean leaveGame(int gameID) throws GameActionException, BadUserException {
        boolean left = serverProxy.leaveGame(getUser(), gameID);
	    requestGames();
	    return left;
    }

    /**
     * Creates a game with current user as the conductor (creator). Throws a GameActionException
     * if user can't join the newly created game (already part of game, the game is full, etc).
     * Throws a BadUserException if getUser() cannot authenticate on the server side during game
     * creation.
     * @param gameName String game name
     * @param playerCount int number of players (this should be between 2 and 5)
     * @param color Player.PlayerColor color desired by the creating user
     * @return Game object representing the newly created game
     */
    public GameInfo createGame(String gameName, int playerCount, PlayerColor color) throws GameActionException, BadUserException {
        GameInfo createdGame = serverProxy.createGame(getUser(), gameName, playerCount);

        if(createdGame == null)
            return null;
        createdGame = joinGame(createdGame.getGameID(), color);
        return createdGame;
    }

    /**
     * Joins the game that matches the gameID and selects the specified color for the user. Throws
     * BadUserException if user cannot be authenticated by server. Throws GameActionException if
     * user can't join the game (already part of the game, the game is full, etc).
     * @param gameID int game ID
     * @param color PlayerColor color
     */
    public GameInfo joinGame(int gameID, PlayerColor color) throws GameActionException, BadUserException {
        GameInfo ret = serverProxy.joinGame(getUser(), gameID, color);
	    requestGames();
	    return ret;
    }

    /**
     * Resets poller state and starts polling for the player list.
     * @param view object implements IView
     */
    public void pollPlayerList(IView view) {
		stopPollingGameStuff();
	    poller.startPollingPlayerList(view);
    }

    /**
     * Resets poller state and starts polling for Commands.
     * @param view object implements IView
     */
    public void pollCurrentGameParts(IView view) {
        stopPollingPlayers();
        poller.startPollingCommands(view);
        poller.startPollingChat(view);
    }

    /**
     * Stops polling (resets poller state).
     */
    public void stopPollingPlayers() {
	    if (poller == null) {
		    poller = new Poller(serverProxy, clientModelRoot);
		    return;
	    }
	    poller.stopPollingPlayers();
    }
	public void stopPollingGameStuff() {
		if (poller == null) {
			poller = new Poller(serverProxy, clientModelRoot);
			return;
		}
		poller.stopPollingCmdChat();
	}

    /**
     * If poller is null, creates a new Poller. Tells poller to stop polling.
     */
	public void stopPollingAll() {
		if (poller == null) {
			poller = new Poller(serverProxy, clientModelRoot);
			return;
		}
		poller.stopPollingAll();
	}

    /**
     * Sets client model current user to null, effectively logging out the user and requiring
     * a new login.
     */
	public void logout() {
		clientModelRoot.setCurrentUser(null);
	}

    // Observer things.

    /**
     * Register an observer for the ClientModel. Your class (a Presenter, probably) should
     * implement Observer and have the required update(...) method. Also, see this for useful
     * visual: http://stackoverflow.com/a/15810174.
     * @param obs Observer object
     */
    public void registerObserver(Observer obs) {
        //System.out.println("register " + obs.getClass().getSimpleName());
	    clientModelRoot.addObserver(obs);
    }

    /**
     * Unregister an observer from the ClientModel.
     * @param obs Observer object
     */
    public void unregisterObserver(Observer obs) {
        //System.out.println("unregister " + obs.getClass().getSimpleName());
	    clientModelRoot.deleteObserver(obs);
    }

    public void clearObservers() {
	    //System.out.println("clear observers");
	    clientModelRoot.deleteObservers();
    }

    // Phase 2 stuff.

    // Score and player related.

    /**
     * Gets the length of player's longest path.
     * @param user User object
     * @return int Length of player's longest path.
     */
    public int getLongestPathPlayer(User user) {
        return getCurrentGame().getPlayer(user).getLongestRoute();
    }

    /**
     * Returns a list of players whose longest paths are the longest path in the game.
     * @return List of Player objects who have the longest path
     */
    public List<Player> getPlayerLongestPath() {
        return getCurrentGame().getPlayerLongestRoute();
    }

    /**
     * Returns the length of the longest route in the current game.
     * @return int length of longest route
     */
    public int getLongestRoute() {
        return getCurrentGame().getLongestRoute();
    }


    /**
     *
     * @return Map of TrainCardColor to Integer
     */
    public Map<TrainCardColor, Integer> getCardMap(User user) {

        return clientModelRoot.getCurrentGame().getPlayer(user).getTrainCards();
    }

    /**
     * Returns the Player whose turn it is.
     * @return Player object
     */
    public Player whoseTurn() {
        return clientModelRoot.whoseTurn();
    }

    // Cards.

    public TrainCard[] getFaceUpCards() {
        return clientModelRoot.getCurrentGame().getBoard().getFaceUpCards();
    }

    public TrainCardColor drawTrainCardFromDeck() throws GameActionException {
	    TrainCardColor color = clientModelRoot.getCurrentGame().drawTrainCardFromDeck(clientModelRoot.getCurrentUser());
	    clientModelRoot.notifyGameChanged();
        return color;
    }


    /** This method adds one train card to another player's hand. Just for animation. **/
    public void animate_AddTrainCardForOtherPlayer(){
        Player p = clientModelRoot.getCurrentGame().getPlayerList().get(0);
        if(clientModelRoot.getCurrentGame().getPlayerList().size() > 1){
            Player p2 = clientModelRoot.getCurrentGame().getPlayerList().get(1);
            TrainCard t = new TrainCard(TrainCardColor.GREEN);
            p2.addTrainCard(t,1);
            System.out.println(t.getColor().name());
            clientModelRoot.notifyGameChanged();
        }
    }

    /** This method also is solely for animation purposes **/
    public void animate_AddDestinationCardForOtherPlayer(){
        Player p = clientModelRoot.getCurrentGame().getPlayerList().get(0);
        if(clientModelRoot.getCurrentGame().getPlayerList().size() > 1){
            Player p2 = clientModelRoot.getCurrentGame().getPlayerList().get(1);
            City c = new City("city");
            DestinationCard d = new DestinationCard(c,c,1,"h");
            p2.addDestinationCard(d);
        }
    }

    /** This method also is solely for animation purposes **/
    public void animate_UpdatePointsForOtherPlayer(){
        Player p = clientModelRoot.getCurrentGame().getPlayerList().get(0);
        if(clientModelRoot.getCurrentGame().getPlayerList().size() > 1){
            Player p2 = clientModelRoot.getCurrentGame().getPlayerList().get(1);
            p2.setScore(p2.getScore() + 4);
        }
    }

    public void animate_ClaimRoute(){
        Player p = clientModelRoot.getCurrentGame().getPlayerList().get(0);
        Route r = clientModelRoot.getCurrentGame().getAvailableRoutes().get(0);
        p.claimRoute(r);
    }

    public void animate_ClaimRouteOtherPlayer(){
        if(clientModelRoot.getCurrentGame().getPlayerList().size() > 1){
            Player p2 = clientModelRoot.getCurrentGame().getPlayerList().get(1);
            Route r = clientModelRoot.getCurrentGame().getAvailableRoutes().get(7);
            p2.claimRoute(r);
        }

    }

    /** This method also is solely for animation purposes **/
    public void animate_sendChatMessage(Message m) throws BadUserException{
        if(clientModelRoot.getChatLog() == null)
        {
            clientModelRoot.setChatLog(new GameChatLog());
        }
        GameChatLog log = clientModelRoot.getChatLog();
        log.addMessage(m);
        clientModelRoot.setChatLog(log);
        clientModelRoot.notifyGameChanged();
    }


    /*
        TYLER, you were questioning if you wanted to implement this or not, but here it is
     */
    public TrainCardColor drawTrainCard(int position) throws GameActionException { // 0,1,2,3,4 for the position of the card that is drawn, top 0, bottom 4
	    //TODO without a deck manager this is always going to throw exceptions
        TrainCardColor color = clientModelRoot.getCurrentGame().drawFaceUpCard(clientModelRoot.getCurrentUser(), position);
	    clientModelRoot.notifyGameChanged();
        return color;
    }

    /*
        TYLER, you were questioning if you wanted to implement this or not, but here it is


     */

    /**
     *
     * @return Array of 3 Destination Cards
     * @throws GameActionException
     */
    public void drawDestinationCards() throws GameActionException {
	    Player player = clientModelRoot.getCurrentGame().getPlayer(getUser());
	    Board board = clientModelRoot.getCurrentGame().getBoard();
		for (int i = 0; i < 3; i++){
			DestinationCard card = board.drawFromDestinationCardDeck();
			if(card != null)
				player.addDestinationCard(card);
			else
				break;
		}
	    clientModelRoot.notifyGameChanged();
    }

    /*
        TYLER, you were questioning if you wanted to implement this or not, but here it is
     */
    public void discardDestinationCard(DestinationCard destinationCard) throws GameActionException {
	    clientModelRoot.getCurrentGame().getPlayer(clientModelRoot.getCurrentUser()).removeDestinationCard(destinationCard);
        clientModelRoot.getCurrentGame().getBoard().discard(destinationCard);
	    clientModelRoot.notifyGameChanged();
    }

	public void stopDiscarding() {
		clientModelRoot.getCurrentGame().getPlayer(clientModelRoot.getCurrentUser()).markAllNotDiscardable();
		clientModelRoot.notifyGameChanged();
	}



    // Routes.

    public void claimRoute(Route route, User user) {
        clientModelRoot.getCurrentGame().claimRoute(route, clientModelRoot.getCurrentGame().getPlayer(user));
	    clientModelRoot.notifyGameChanged();
    }

    public List<Route> getAvailableRoutes() {
        return clientModelRoot.getCurrentGame().getAvailableRoutes();
    }

    public Boolean canClaimRoute(Route route) {
	    Player player = clientModelRoot.getCurrentGame().getPlayer(clientModelRoot.getCurrentUser());
	    return route.canClaim(player);
    }

    public List<Route> getRoutes() {
        return clientModelRoot.getCurrentGame().getRoutes();
    }

    // Chat functions.

    public GameChatLog getChatMessages() {
        return clientModelRoot.getChatLog();
    }

    public void sendChatMessage(Message message) throws BadUserException {
        serverProxy.sendChatMessage(clientModelRoot.getCurrentUser(), message);
    }
}
