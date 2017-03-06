package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.commands.CommandManager;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by mgard on 2/14/2017.
 */

public class Poller {

	private ServerProxy serverProxy = null;
	private CommandManager commandManager = null;
	private ScheduledExecutorService playerPollSES = null;
	private ScheduledExecutorService commandPollSES = null;
	private ScheduledExecutorService chatPollSES = null;


	public Poller(ServerProxy sp, ClientModel cm) {
		serverProxy = sp;
		commandManager = new CommandManager(cm);
	}

	public void startPollingPlayerList(final IView view) {
		playerPollSES = Executors.newScheduledThreadPool(1);
		playerPollSES.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                view.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
	                    if(commandManager.currentGameID() > -1) {
		                    try {
			                    System.out.println("requesting player list");
			                    Game game = serverProxy.getGame(commandManager.getUser(), commandManager.currentGameID());
			                    commandManager.setPlayerList(game.getPlayerList());
			                    //TODO probably make this set the game so on start we have all the basics.
		                    } catch(BadUserException e) {
			                    e.printStackTrace();
			                    view.backToLogin();
			                    stopPollingAll();
		                    }
	                    } else {
		                    stopPollingAll();
	                    }
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
	}

	public void startPollingCommands(final IView view) {
		commandPollSES = Executors.newScheduledThreadPool(1);
		commandPollSES.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				view.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(commandManager.currentGameID() > -1) {
							try {
								System.out.println("getting commands");
								ArrayList<ICommand> commands = serverProxy.getCommandsSince(commandManager.getUser(), commandManager.currentGameID(), commandManager.getLastCommandExecuted());
								commandManager.addCommands(commands);
							} catch(BadUserException e) {
								e.printStackTrace();
								view.backToLogin();
								stopPollingAll();
							} catch(GameActionException e) {
								e.printStackTrace();
							}
						} else {
							stopPollingAll();
						}
					}
				});
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void startPollingChat(final IView view) {
		chatPollSES = Executors.newScheduledThreadPool(1);
		chatPollSES.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				view.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(commandManager.currentGameID() > -1) {
							try {
								System.out.println("getting chat log");
								GameChatLog gameChatLog = serverProxy.getChatLog(commandManager.getUser(), commandManager.getGame().getGameInfo());
								commandManager.getClientFacade().setChatLog(gameChatLog);
								//TODO this is an awful way to do this.
							} catch(BadUserException e) {
								e.printStackTrace();
								view.backToLogin();
								stopPollingAll();
							}
						} else {
							stopPollingAll();
						}
					}
				});
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void stopPollingAll() {
		if(chatPollSES != null)
			chatPollSES.shutdown();
		if(playerPollSES != null)
			playerPollSES.shutdown();
		if(commandPollSES != null)
			commandPollSES.shutdown();
	}

	public void setClientModel(ClientModel cm) {
		commandManager.setClientModel(cm);
	}

}
