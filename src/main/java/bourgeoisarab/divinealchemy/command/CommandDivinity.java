package bourgeoisarab.divinealchemy.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;

public class CommandDivinity extends CommandBase {

	@Override
	public String getCommandName() {
		return "divinity";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return getCommandUsage();
	}

	public String getCommandUsage() {
		return "commands.divinity.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayer player = null;
		float divinity = 0.0F;
		if (args.length >= 1) {
			if (args[0].equals("get")) {
				if (args.length == 2) {
					player = getPlayer(sender, args[1]);
					if (player == null) {
						throw new PlayerNotFoundException();
					}
				} else if (args.length == 1) {
					player = getCommandSenderAsPlayer(sender);
				} else {
					throw new WrongUsageException(getCommandUsage());
				}
				divinity = NBTPlayerHelper.getDivinity(player);
				// ??? String.format("%.4f", divinity);
				sender.addChatMessage(new ChatComponentText(I18n.format("commands.divinity.getDivinity", player.getCommandSenderName(), divinity)));
			} else {
				if (args.length == 3) {
					player = getPlayer(sender, args[1]);
					if (player == null) {
						throw new PlayerNotFoundException();
					}
					divinity = getDivinity(args[2]);
				} else if (args.length == 2) {
					player = getCommandSenderAsPlayer(sender);
					divinity = getDivinity(args[1]);
				} else {
					throw new WrongUsageException(getCommandUsage());
				}

				if (args[0].equals("add")) {
					NBTPlayerHelper.addDivinity(player, divinity);
					sender.addChatMessage(new ChatComponentText(I18n.format("commands.divinity.addDivinity", player.getCommandSenderName(), divinity)));
				} else if (args[0].equals("set")) {
					NBTPlayerHelper.setDivnity(player, divinity);
					sender.addChatMessage(new ChatComponentText(I18n.format("commands.divinity.setDivinity", player.getCommandSenderName(), divinity)));
				}
			}
		} else {
			throw new WrongUsageException(getCommandUsage());
		}
	}

	private float getDivinity(String s) {
		float f;
		try {
			f = Float.parseFloat(s);
		} catch (Exception e) {
			throw new WrongUsageException(getCommandUsage());
		}
		if (f > 1.0F || f < -1.0F) {
			throw new WrongUsageException(I18n.format("commands.divinity.outOfBounds"));
		}
		return f;
	}
}
