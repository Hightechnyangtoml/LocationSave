package kr.cafemoca.locationsave;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LocationSaveTabCompleter implements TabCompleter {
	private LocationSave plugin;

	public LocationSaveTabCompleter(LocationSave plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 1) {
			completions.add("save");
			completions.add("delete");
			completions.add("list");
			completions.add("warp");
			completions.add("help");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("save")) {
				completions.add("<name>");
			} else if (args[0].equalsIgnoreCase("delete")) {
				for (String location : plugin.getLocationConfig().getKeys(false)) {
					completions.add(location);
				}
			} else if (args[0].equalsIgnoreCase("warp")) {
				for (String location : plugin.getLocationConfig().getKeys(false)) {
					completions.add(location);
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("save")) {
				completions.add("<x>");
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("save")) {
				completions.add("<y>");
			}
		} else if (args.length == 5) {
			if (args[0].equalsIgnoreCase("save")) {
				completions.add("<z>");
			}
		}

		return completions;
	}
}