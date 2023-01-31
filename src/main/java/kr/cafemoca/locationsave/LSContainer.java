package kr.cafemoca.locationsave;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LSContainer {
	private final File locationsFile = new File(LSPlugin.getInstance().getDataFolder(), "locations.yml");
	private FileConfiguration locationsConfig;

	public LSContainer() {
		if (!locationsFile.exists()) {
			try {
				locationsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		locationsConfig = YamlConfiguration.loadConfiguration(locationsFile);
	}

	public void saveLocation(Player player, String name, String[] args) {
		int x, y, z;
		if (args.length > 4) {
			try {
				x = Integer.parseInt(args[2]);
				y = Integer.parseInt(args[3]);
				z = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage(LSPlugin.getInstance().getConfig().getString("ls.save.locationerror"));
				return;
			}
		} else {
			x = (int) player.getLocation().getX();
			y = (int) player.getLocation().getY();
			z = (int) player.getLocation().getZ();
		}
		locationsConfig.set(name + ".x", x);
		locationsConfig.set(name + ".y", y);
		locationsConfig.set(name + ".z", z);
		try {
			locationsConfig.save(locationsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.sendMessage(String.format(LSPlugin.getInstance().getConfig().getString("ls.save.success"), name));
	}

	public void listLocations(Player player) {
		StringBuilder sb = new StringBuilder();
		for (String key : locationsConfig.getKeys(false)) {
			sb.append(key).append(" ");
		}
		player.sendMessage(String.format(LSPlugin.getInstance().getConfig().getString("ls.list"), sb.toString()));
	}

	public void warpToLocation(Player player, String name) {
		if (!locationsConfig.contains(name)) {
			player.sendMessage(String.format(LSPlugin.getInstance().getConfig().getString("ls.warp.notfound"), name));
			return;
		}
		int x = locationsConfig.getInt(name + ".x");
		int y = locationsConfig.getInt(name + ".y");
		int z = locationsConfig.getInt(name + ".z");
		player.teleport(new Location(player.getWorld(), x, y, z));
		player.sendMessage(String.format(LSPlugin.getInstance().getConfig().getString("ls.warp.success"), name));
	}
}
