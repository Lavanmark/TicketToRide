package com.floorcorn.tickettoride.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tyler on 3/1/17.
 */

public class MapFactory {

	private static final String FILE_STRING = "./shared/src/main/java/com/floorcorn/tickettoride/model/maps/";
	private static final String FILE_STRING_CITIES = "cities/";
	private static final String FILE_STRING_ROUTES = "routes/";
	private static final String FILE_STRING_DEST = "dest/";

	private static List<Route> marsRoutes = null;
	private static List<DestinationCard> marsDest = null;

	private void createMarsMap() {
		Set<City> marsCities = readCities("MarsCities.csv");
		marsRoutes = readRoutes("MarsRoutes.csv", marsCities);
		marsDest = readDestionationCards("MarsDestinationCards.csv", marsCities);
	}

	public List<Route> getMarsRoutes() {
		if(marsRoutes == null || marsDest == null)
			createMarsMap();
		return new ArrayList<>(marsRoutes);
	}

	public List<DestinationCard> getMarsDestinationCards() {
		if(marsRoutes == null || marsDest == null)
			createMarsMap();
		return new ArrayList<>(marsDest);
	}

	private Set<City> readCities(String fileName) {
		File f = new File(FILE_STRING + FILE_STRING_CITIES + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			Set<City> cities = new HashSet<>();
			while((line = br.readLine()) != null) {
				if(!line.isEmpty())
					cities.add(new City(line));
			}
			return cities;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new HashSet<>();
	}

	private List<Route> readRoutes(String fileName, Set<City> cities) {
		File f = new File(FILE_STRING + FILE_STRING_ROUTES + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			List<Route> routes = new ArrayList<>();
			int routeID = 0;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length == 4) {
					City city1 = new City(parts[0]);
					City city2 = new City(parts[1]);
					TrainCardColor color = TrainCardColor.convertString(parts[2]);
					int length = Integer.valueOf(parts[3]);
					if(cities.contains(city1) && cities.contains(city2))
						routes.add(new Route(routeID++, city1, city2, length, color));
				}
			}
			return routes;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	private List<DestinationCard> readDestionationCards(String fileName, Set<City> cities) {
		File f = new File(FILE_STRING + FILE_STRING_DEST + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			List<DestinationCard> dest = new ArrayList<>();
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length == 3) {
					City city1 = new City(parts[0]);
					City city2 = new City(parts[1]);
					int value = Integer.valueOf(parts[2]);
					if(cities.contains(city1) && cities.contains(city2))
						dest.add(new DestinationCard(city1, city2, value));
				}
			}
			return dest;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
