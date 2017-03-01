package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.commands.CommandManager;
import com.floorcorn.tickettoride.commands.ICommand;
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
	private ScheduledExecutorService scheduledExecutorService = null;

	public Poller(ServerProxy sp, ClientModel cm) {
		serverProxy = sp;
		commandManager = new CommandManager(cm);
	}

	public void startPollingPlayerList(final IView view) {
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

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
		                    } catch(BadUserException e) {
			                    e.printStackTrace();
			                    view.backToLogin();
			                    stopPolling();
		                    }
	                    } else {
		                    stopPolling();
	                    }
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
	}

	public void startPollingCommands(final IView view) {
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				view.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(commandManager.currentGameID() > -1) {
							try {
								ArrayList<ICommand> commands = serverProxy.getCommandsSince(commandManager.getUser(), commandManager.currentGameID(), commandManager.getLastCommandExecuted());
								commandManager.addCommands(commands);
							} catch(BadUserException e) {
								e.printStackTrace();
								view.backToLogin();
								stopPolling();
							} catch(GameActionException e) {
								e.printStackTrace();
							}
						} else {
							stopPolling();
						}
					}
				});
			}
		}, 0, 5, TimeUnit.SECONDS);
	}

	public void stopPolling() {
		if(scheduledExecutorService != null)
			scheduledExecutorService.shutdown();
	}

	public void setClientModel(ClientModel cm) {
		commandManager.setClientModel(cm);
	}

}
