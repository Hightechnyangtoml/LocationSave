package kr.cafemoca.locationsave;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LSCommand implements CommandExecutor {
	private final LSContainer container = new LSContainer();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			player.sendMessage(LSPlugin.getInstance().getConfig().getString("ls.help"));
			return false;
		}

		switch (args[0].toLowerCase()) {
			case "save":
				if (args.length < 2) {
					player.sendMessage(LSPlugin.getInstance().getConfig().getString("ls.save.error"));
					return false;
				}
				container.saveLocation(player, args[1], args);
				break;
			case "list":
				container.listLocations(player);
				break;
			case "warp":
				if (args.length < 2) {
					player.sendMessage(LSPlugin.getInstance().getConfig().getString("ls.warp.error"));
					return false;
				}
				container.warpToLocation(player, args[1]);
				break;
			default:
				player.sendMessage(LSPlugin.getInstance().getConfig().getString("ls.help"));
				break;
		}

		return true;
	}
}
