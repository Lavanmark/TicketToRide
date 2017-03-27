package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.log.Corn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by Tyler on 3/1/17.
 *
 * @author Tyler
 * @author Michael
 */

/**
 * This class makes maps for Ticket to Ride, meaning it makes lists of routes and destination
 * cards and things like that. It reads the info from .csv files.
 *
 * @invariant the directory indicated by the FILE_STRING exists
 */
public class MapFactory {

	/** The default string to access the maps directory where the .csv files are stored.
	 * File.separator allows this string to be used across multiple platforms.
	 */
	private static String FILE_STRING = "."
            + File.separator + "shared"
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "java"
            + File.separator + "com"
            + File.separator + "floorcorn"
            + File.separator + "tickettoride"
            + File.separator + "model"
            + File.separator + "maps"
            + File.separator;
	/** Extension for the cities .csv **/
	private static final String FILE_STRING_CITIES = "cities" + File.separator;
	/** Extension for the routes .csv **/
	private static final String FILE_STRING_ROUTES = "routes" + File.separator;
	/** Extension for the destination cards .csv **/
	private static final String FILE_STRING_DEST = "dest" + File.separator;

	/** The list of routes for the given game **/
	private List<Route> marsRoutes = null;
	/** The list of destination cards for the game **/
	private List<DestinationCard> marsDest = null;

	/** This methods calls the respective import methods for each type of .csv, namely
	 * routes, cities, and destination cards.
	 *
	 * @pre the .csv "MarsCities.csv" exists and is not empty
	 * @pre the "MarsRoutes.csv" exists and is not empty
	 * @pre the "MarsDestinationCards.csv" file exists and is not empty
	 *
	 * @post the marsRoutes list has the same data that is in the "MarsRoutes.csv" file
	 * 		but the data is in the correct object form.
	 * @post the marsDest list has the same data as what is in the "MarsDestinationCards.csv"
	 * 		file, but in correct object form.
	 * @post neither of the two lists, marsRoutes and marsDest are null
	 */
	private void createMarsMap() {
		Set<City> marsCities = readCities("MarsCities.csv");
        marsRoutes = readRoutes("MarsRoutes.csv", marsCities);
        marsDest = readDestionationCards("MarsDestinationCards.csv", marsCities);

		//Note: Comment out the above 3 lines and uncomment those 3 below to test.
        /*Set<City> testCities = readCities("TestCities.csv");
        marsRoutes = readRoutes("TestRoutes.csv", testCities);
        marsDest = readDestionationCards("TestDestinationCards.csv", testCities);
		*/
	}

	/**
	 * This method returns the list of routes if it exists, otherwise it calls
	 * createMarsMap() in order to have the data imported from the .csv files
	 *
	 * @return an ArrayList<Route> representing all of the routes for the game
	 *
	 * @pre the correct .csv files exist
	 * @post retVal.size() == count(routesInFile)
	 * @post retVal != null
     */
	public List<Route> getMarsRoutes() {
		if(marsRoutes == null || marsDest == null)
			createMarsMap();
		return new ArrayList<>(marsRoutes);
	}

	/**
	 * This method returns the list of destination cards if it exists, otherwise it calls
	 * createMarsMap() in order to have the data imported from the .csv files
	 *
	 * @return an ArrayList<Route> representing all of the destination cards for the game
	 *
	 * @pre the correct .csv files exist
	 * @post retVal.size() == count(destinationCardsInFile)
	 * @post retVal != null
	 */
	public List<DestinationCard> getMarsDestinationCards() {
		if(marsRoutes == null || marsDest == null)
			createMarsMap();
		return new ArrayList<>(marsDest);
	}

	/**
	 * This method reads in the cites from the given .csv file and creates the appropriate
	 * number of City objects.
	 * @param fileName the String representing the .csv file where the cities are contained.
	 * @return the Set<City> representing all of the cities in the designated file.
	 *
	 * @pre fileName refers to a file that exists in the correct directory.
	 *
	 * @post retVal != null
	 * @post retVal.size() == count(CitiesInFile)
     */
	private Set<City> readCities(String fileName) {
		File f = new File(FILE_STRING + FILE_STRING_CITIES + fileName);
        if(!f.canRead()) {
            Corn.log(Level.SEVERE, "Unable to read from specified input file");
        }
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			Set<City> cities = new HashSet<>();
			while((line = br.readLine()) != null) {
				if(!line.isEmpty())
					cities.add(new City(line));
			}
			br.close();
			return cities;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new HashSet<>();
	}

	/**
	 * This method reads in the routes from the given .csv file and creates the appropriate
	 * number of Route objects.
	 * @param fileName the String representing the .csv file where the Routes are contained.
	 * @param cities the Set<City> which represents the cities which compose the routes.
	 * @return the List<Route> representing all of the routes in the designated file.
	 *
	 * @pre fileName refers to a file that exists in the correct directory.
	 * @pre cities != null
	 *
	 * @post retVal != null
	 * @post retVal.size() == count(RoutesInFile)
	 */
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
			br.close();
			return routes;
		} catch(IOException e) {
			e.printStackTrace();
			Corn.log(Level.SEVERE, e.getMessage() + e.getStackTrace());
		}
		return new ArrayList<>();
	}

	/**
	 * This method reads in the destination cards from the given .csv file and creates the appropriate
	 * number of DestinationCard objects.
	 * @param fileName the String representing the .csv file where the DestinationsCards are contained.
	 * @param cities the Set<City> which represents the cities which compose the routes.
	 * @return the List<Route> representing all of the routes in the designated file.
	 *
	 * @pre fileName refers to a file that exists in the correct directory.
	 * @pre cities != null
	 *
	 * @post retVal != null
	 * @post retVal.size() == count(DestinationCardsInFile)
	 */
	private List<DestinationCard> readDestionationCards(String fileName, Set<City> cities) {
		File f = new File(FILE_STRING + FILE_STRING_DEST + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			List<DestinationCard> dest = new ArrayList<>();
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length == 4) {
					City city1 = new City(parts[0]);
					City city2 = new City(parts[1]);
					int value = Integer.valueOf(parts[2]);
					String resource = parts[3];
					if(cities.contains(city1) && cities.contains(city2))
						dest.add(new DestinationCard(city1, city2, value, resource));
				}
			}
			br.close();
			return dest;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

    /**
     * This method allows the default FILE_STRING to be updated to another path.
     * @param path the new file path for the .csv files to be imported from.
     *
     * @pre path != null
     * @pre path exists
     *
     * @post this.FILE_STRING.equals(path)
     */
    public void setFilePath(String path){
        FILE_STRING = path;
    }

}
