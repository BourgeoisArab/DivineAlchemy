package bourgeoisarab.divinealchemy.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;

public class CommandDivinity extends CommandBase {

	@Override
	public String getCommandName() {
		return "divinity";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.divinity.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayer player = null;
		float divinity = 0.0F;
		if (args.length == 2) {
			player = getCommandSenderAsPlayer(sender);
			divinity = getDivinity(sender, args[1]);
		} else if (args.length == 3) {
			player = getPlayer(sender, args[1]);
			if (player == null) {
				throw new PlayerNotFoundException();
			}
			divinity = getDivinity(sender, args[2]);
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		if (args[0].equals("add")) {
			NBTPlayerHelper.addDivinity(player, divinity);
		} else if (args[0].equals("set")) {
			NBTPlayerHelper.setDivnity(player, divinity);
		} else if (args[0].equals("get")) {
		} else {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		sender.addChatMessage(new ChatDivinitySet(player, divinity));
	}

	private float getDivinity(ICommandSender sender, String s) {
		float f;
		try {
			f = Float.parseFloat(s);
		} catch (Exception e) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		if (f > 1.0F || f < -1.0F) {
			throw new WrongUsageException(I18n.format("commands.divinity.outOfBounds"));
		}
		return f;
	}
}
