package kr.cafemoca.locationsave;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class LocationSaveCommand implements CommandExecutor {
	private LocationSave plugin;

	public LocationSaveCommand(LocationSave plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, plugin.getConfig().getString("ls.message.only-player"));
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-args"));
			return false;
		}

		if (args[0].equalsIgnoreCase("save")) {
			if (args.length < 2) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-args"));
				return false;
			}

			String name = args[1];
			Location location;

			if (plugin.getLocationConfig().contains(name)) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.already-exists").replace("{name}", name));
				return false;
			}

			try {
				if (args.length == 2) {
					Location playerLocation = player.getLocation();
					int x = (int) playerLocation.getX();
					int y = (int) playerLocation.getY();
					int z = (int) playerLocation.getZ();
					location = new Location(player.getWorld(), x, y, z);

				} else {
					int x = Integer.parseInt(args[2]);
					int y = Integer.parseInt(args[3]);
					int z = Integer.parseInt(args[4]);
					location = new Location(player.getWorld(), x, y, z);

				}
				plugin.saveLocation(name, location);
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.save-success").replace("{name}", name).replace("{x}", String.valueOf(location.getBlockX())).replace("{y}", String.valueOf(location.getBlockY())).replace("{z}", String.valueOf(location.getBlockZ())));

			}catch (NumberFormatException e) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.type-not-correct"));
			}

		} else if (args[0].equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-args"));
				return false;
			}

			String name = args[1];

			if (!plugin.getLocationConfig().contains(name)) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.not-found").replace("{name}", name));
				return false;
			}

			plugin.deleteLocation(name);
			plugin.sendMessage(player, plugin.getConfig().getString("ls.message.delete-success").replace("{name}", name));
		} else if (args[0].equalsIgnoreCase("list")) {
			List<String> locations = plugin.getLocations();

			if (locations.isEmpty()) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.empty"));
				return false;
			}
			int page = 1;
			int maxPage = (int) Math.ceil((double) locations.size() / 7);

			if (args.length == 2) {
				try {
					page = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-page").replace("{maxPage}", String.valueOf(maxPage)));
					return false;
				}
			}

			if (page < 1 || page > maxPage) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-page"));
				return false;
			}
			if (locations == null) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.empty"));
				return false;
			}

			int startIndex = (page - 1) * 7;
			int endIndex = Math.min(startIndex + 7, locations.size());
			List<String> displayLocations = locations.subList(startIndex, endIndex);

			plugin.sendMessage(player, plugin.getConfig().getString("ls.message.list-header").replace("{page}", String.valueOf(page)).replace("{maxPage}", String.valueOf(maxPage)));
			for (String location : displayLocations) {
				Location loc = plugin.getLocation(location);
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.list-item").replace("{name}", location).replace("{x}", String.valueOf(loc.getBlockX())).replace("{y}", String.valueOf(loc.getBlockY())).replace("{z}", String.valueOf(loc.getBlockZ())));
			}
		} else if (args[0].equalsIgnoreCase("warp")) {
			if (args.length < 2) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-args"));
				return false;
			}

			String name = args[1];

			if (!plugin.getLocationConfig().contains(name)) {
				plugin.sendMessage(player, plugin.getConfig().getString("ls.message.not-found").replace("{name}", name));
				return false;
			}

			Location location = plugin.getLocation(name);
			player.teleport(location);
			plugin.sendMessage(player, plugin.getConfig().getString("ls.message.warp-success").replace("{name}", name));
		} else if (args[0].equalsIgnoreCase("help")) {
			plugin.sendMessage(player, "/ls save [name] <x> <y> <z> - 해당 좌표의 위치를 [name] 으로 저장합니다. (생략 가능)");
			plugin.sendMessage(player, "/ls delete [name] - [name]의 위치를 삭제합니다.");
			plugin.sendMessage(player, "/ls list - 현재 저장된 위치를 출력합니다.");
			plugin.sendMessage(player, "/ls warp [name] - [name] 으로 워프합니다.");
			plugin.sendMessage(player, "/ls help - 이 메세지를 출력합니다.");
		} else {
			plugin.sendMessage(player, plugin.getConfig().getString("ls.message.invalid-args"));
			return false;
		}

		return true;
	}
}