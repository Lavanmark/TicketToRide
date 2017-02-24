package com.floorcorn.tickettoride;


import android.app.Activity;

import com.floorcorn.tickettoride.clientModel.ClientModel;
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
	private ClientFacade clientFacade = null;//TODO change to client facade when done
	private ScheduledExecutorService scheduledExecutorService = null;

	public Poller(ServerProxy sp, ClientModel cm) {
		serverProxy = sp;
		clientFacade = new ClientFacade(cm);
	}

	public void startPollingPlayerList(final IView view) {
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                view.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
	                    if(clientFacade.getGame() != null) {
		                    try {
			                    System.out.println("requesting player list");
			                    Game game = serverProxy.getGame(clientFacade.getUser(), clientFacade.getGame().getGameID());
			                    clientFacade.setPlayerList(game.getPlayerList());
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
						if(clientFacade.getGame() != null) {
							try {
								ArrayList<ICommand> commands = serverProxy.getCommandsSince(clientFacade.getUser(), clientFacade.getGame().getGameID(), clientFacade.getLastExecutedCommand());

								Game game = clientFacade.getGame();
								if(game == null) {
									stopPolling();
									return;
								}

								if(commands == null || commands.size() == 0)
									return;

								for(ICommand command : commands) {
									game.addCommand(command);
									command.execute();
								}
								//TODO maybe update to make the clientfacade do this leg work
								clientFacade.updateGame(game);
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
		clientFacade.setClientModel(cm);
	}

}
