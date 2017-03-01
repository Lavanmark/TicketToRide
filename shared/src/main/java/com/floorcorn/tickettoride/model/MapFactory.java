package com.floorcorn.tickettoride.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 3/1/17.
 */

public class MapFactory {

	private List<Route> marsRoutes = null;

	public MapFactory() {
		createMarsMap();
	}
	private void createMarsMap() {
		marsRoutes = new ArrayList<>();
		//TODO make csv importer
		//TODO also import city list;
	}

	public List<Route> getMarsRoutes() {
		if(marsRoutes == null)
			createMarsMap();
		return marsRoutes;
	}
}
