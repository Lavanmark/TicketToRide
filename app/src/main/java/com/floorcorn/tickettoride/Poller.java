package com.floorcorn.tickettoride;


import android.app.Activity;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by mgard on 2/14/2017.
 */

public class Poller {

	private ServerProxy serverProxy = null;
	private ClientModel clientModel = null;//TODO change to client facade when done
	private ScheduledExecutorService scheduledExecutorService = null;

	public Poller(ServerProxy sp, ClientModel cm) {
		serverProxy = sp;
		clientModel = cm;
	}

	public void startPollingPlayerList(final IView view) {
		scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                view.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
	                    if(clientModel.getCurrentGame() != null) {
		                    try {
			                    //TODO update this to only get the player list
			                    clientModel.setCurrentGame(serverProxy.getGame(clientModel.getCurrentUser(), clientModel.getCurrentGame().getGameID()));
		                    } catch(BadUserException e) {
			                    e.printStackTrace();
			                    view.backToLogin();
			                    stopPolling();
		                    }
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

}
