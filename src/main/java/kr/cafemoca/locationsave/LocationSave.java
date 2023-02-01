package kr.cafemoca.locationsave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationSave extends JavaPlugin implements Listener {
	private FileConfiguration config;
	private File configFile;

	private FileConfiguration locationConfig;
	private File locationConfigFile;

	@Override
	public void onEnable() {
		configFile = new File(getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);

		locationConfigFile = new File(getDataFolder(), "location.yml");
		locationConfig = YamlConfiguration.loadConfiguration(locationConfigFile);

		saveDefaultConfig();
		saveLocationConfig();

		getCommand("ls").setExecutor(new LocationSaveCommand(this));
		getCommand("ls").setTabCompleter(new LocationSaveTabCompleter(this));

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	public FileConfiguration getLocationConfig() {
		return locationConfig;
	}

	public void saveLocationConfig() {
		try {
			locationConfig.save(locationConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveLocation(String name, Location location) {
		locationConfig.set(name + ".x", location.getX());
		locationConfig.set(name + ".y", location.getY());
		locationConfig.set(name + ".z", location.getZ());
		saveLocationConfig();
	}

	public Location getLocation(String name) {
		if (locationConfig.contains(name)) {
			double x = locationConfig.getDouble(name + ".x");
			double y = locationConfig.getDouble(name + ".y");
			double z = locationConfig.getDouble(name + ".z");
			if (x < 0) { x = x-0.5; }
			if (x > 0) { x = x+0.5; }
			if (z < 0) { z = z-0.5; }
			if (z > 0) { z = z+0.5; }
			return new Location(Bukkit.getWorlds().get(0), x, y, z);
		}
		return null;
	}

	public void deleteLocation(String name) {
		locationConfig.set(name, null);
		saveLocationConfig();
	}

	public List<String> getLocations() {
		List<String> locations = new ArrayList<>();
		for (String key : locationConfig.getKeys(false)) {
			locations.add(key);
		}
		return locations;
	}

	public void sendMessage(CommandSender p, String message) {
		String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
		if (coloredMessage == null) {
			coloredMessage = "";
		}
		p.sendMessage(coloredMessage);
	}
}