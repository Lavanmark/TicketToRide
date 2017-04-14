package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.commands.ClaimRouteCmd;
import com.floorcorn.tickettoride.commands.CommandManager;
import com.floorcorn.tickettoride.commands.DiscardDestinationCmd;
import com.floorcorn.tickettoride.commands.DrawDestinationCmd;
import com.floorcorn.tickettoride.commands.DrawTrainCardCmd;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.CommandRequestException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.ui.views.IView;

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
	private CommandManager commandManager;
    private Poller poller;

    // Things relating to private constructor and singleton pattern.

    private UIFacade() {
        clientModelRoot = new ClientModel();
	    commandManager = new CommandManager(clientModelRoot);
        serverProxy = new ServerProxy();
        // Special alias to your host loopback interface (i.e., 127.0.0.1 on your development
        // machine). Don't need to change if hooking to server on your computer.
        setServer("10.0.2.2", "8080");
        poller = new Poller(serverProxy, commandManager);
    }
    private static UIFacade instance = null;
    public static UIFacade getInstance() {
        if (instance == null)
            instance = new UIFacade();
        return instance;
    }

    public void setServer(String host, String port){
        System.out.println("setting Server" + host +":"+ port);
        serverProxy.setPort(port);
        serverProxy.setHost(host);
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
	    GameInfo createdGame = null;
	    try {
		    createdGame = serverProxy.createGame(getUser(), gameName, playerCount);
	    } catch(GameCreationException e) {
		    e.printStackTrace();
	    }
	
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
		    poller = new Poller(serverProxy, commandManager);
		    return;
	    }
	    poller.stopPollingPlayers();
    }
	public void stopPollingGameStuff() {
		if (poller == null) {
			poller = new Poller(serverProxy, commandManager);
			return;
		}
		poller.stopPollingCmdChat();
	}

    /**
     * If poller is null, creates a new Poller. Tells poller to stop polling.
     */
	public void stopPollingAll() {
		if (poller == null) {
			poller = new Poller(serverProxy, commandManager);
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
     * Returns the Player whose turn it is.
     * @return Player object
     */
    public Player whoseTurn() {
        return clientModelRoot.whoseTurn();
    }

    // Cards.

    public TrainCard[] getFaceUpCards() {
        return getCurrentGame().getBoard().getFaceUpCards();
    }

    public TrainCardColor drawTrainCardFromDeck(boolean firstDraw) throws GameActionException, BadUserException {
	    return drawTrainCard(-1, firstDraw);
    }

    public TrainCardColor drawTrainCard(int position, boolean firstDraw) throws GameActionException, BadUserException { // 0,1,2,3,4 for the position of the card that is drawn, top 0, bottom 4
        ICommand cmd = new DrawTrainCardCmd(getCurrentGame().getPlayer(getUser().getUserID()), firstDraw, position);
	    cmd.setCmdID(getCurrentGame().getLatestCommandID());
	    cmd.setGameID(getCurrentGame().getGameID());
	    List<ICommand> res = null;
	    try {
		    res = serverProxy.doCommand(getUser(), cmd);
	    } catch(CommandRequestException e) {
		    e.printStackTrace();
		    requestGame(getCurrentGame().getGameInfo());
		    return null;
	    }
	    commandManager.addCommands(res);
        return getTrainCardColor(res);
    }

    private TrainCardColor getTrainCardColor(List<ICommand> commands){
        for (ICommand c: commands) {
            if(c instanceof DrawTrainCardCmd)
                return ((DrawTrainCardCmd)c).getColor();
        }
        return null;
    }

    /**
     *
     * @return Array of 3 Destination Cards
     * @throws GameActionException
     */
    public void drawDestinationCards() throws GameActionException, BadUserException {
	    ICommand cmd = new DrawDestinationCmd(getCurrentGame().getPlayer(getUser().getUserID()));
	    cmd.setCmdID(getCurrentGame().getLatestCommandID());
	    cmd.setGameID(getCurrentGame().getGameID());
	    List<ICommand> res = null;
	    try {
		    res = serverProxy.doCommand(getUser(), cmd);
	    } catch(CommandRequestException e) {
		    e.printStackTrace();
		    requestGame(getCurrentGame().getGameInfo());
		    return;
	    }
	    commandManager.addCommands(res);
    }

    public void discardDestinationCards(List<DestinationCard> destinationCards) throws GameActionException, BadUserException {
	    DestinationCard[] cardz = new DestinationCard[2];
	    ICommand cmd = new DiscardDestinationCmd(getCurrentGame().getPlayer(getUser().getUserID()), destinationCards.toArray(cardz));
	    cmd.setCmdID(getCurrentGame().getLatestCommandID());
	    cmd.setGameID(getCurrentGame().getGameID());
	    List<ICommand> res = null;
	    try {
		    res = serverProxy.doCommand(getUser(), cmd);
	    } catch(CommandRequestException e) {
		    e.printStackTrace();
		    requestGame(getCurrentGame().getGameInfo());
		    return;
	    }
	    commandManager.addCommands(res);
    }

    // Routes.

    public void claimRoute(Route route) throws BadUserException, GameActionException {
	    ICommand cmd = new ClaimRouteCmd(getCurrentGame().getPlayer(getUser().getUserID()), route);
	    cmd.setCmdID(getCurrentGame().getLatestCommandID());
	    cmd.setGameID(getCurrentGame().getGameID());
	    List<ICommand> res = null;
	    try {
		    res = serverProxy.doCommand(getUser(), cmd);
	    } catch(CommandRequestException e) {
		    e.printStackTrace();
		    requestGame(getCurrentGame().getGameInfo());
		    return;
	    }
	    commandManager.addCommands(res);
    }

    public Boolean canClaimRoute(Route route) {
	    Player player = getCurrentGame().getPlayer(getUser().getUserID());
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
