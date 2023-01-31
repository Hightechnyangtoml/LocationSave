package kr.cafemoca.locationsave;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class LSPlugin extends JavaPlugin {
	private static LSPlugin instance;
	private FileConfiguration config;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		config = getConfig();

		getCommand("ls").setExecutor(new LSCommand());
	}

	public static LSPlugin getInstance() {
		return instance;
	}

	public FileConfiguration getConfig() {
		return config;
	}
}
