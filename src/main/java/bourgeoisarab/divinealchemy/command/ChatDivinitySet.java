package bourgeoisarab.divinealchemy.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;

public class ChatDivinitySet extends ChatComponentText {

	public ChatDivinitySet(EntityPlayer player, float divinity) {
		super("Divinity of " + player.getCommandSenderName() + " has been set to " + divinity);
	}

	public ChatDivinitySet(EntityPlayer player) {
		super("Divinity of " + player.getCommandSenderName() + " is " + NBTPlayerHelper.getDivinity(player));
	}

}
