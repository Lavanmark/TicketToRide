package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.model.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tyler on 4/12/2017.
 */

public class PluginFactory {
	private String pluginFolderLocation = "plugins/";
	private final String configFileName = "config.config";
	private IDAOFactory factory = null;
	
	public void loadPlugins() throws FileNotFoundException {
		File file = new File(pluginFolderLocation + configFileName);
		if(!file.canRead()) {
			throw new FileNotFoundException("Could not find config file!");
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String mainClass = null;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					if(line.contains("MainClass:"))
						mainClass = line.split(":")[1];
				}
			}
			br.close();
			if(mainClass == null)
				return;
			Class<?> c = null;
			try {
				c = Class.forName(mainClass);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}
			factory = (IDAOFactory)c.newInstance();
		} catch(IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	public IDAOFactory getDAOFactory() {
		return factory;
	}
}
