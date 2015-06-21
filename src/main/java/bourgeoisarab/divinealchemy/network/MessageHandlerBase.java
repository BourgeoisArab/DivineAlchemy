package bourgeoisarab.divinealchemy.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class MessageHandlerBase<REQ extends IMessage> implements IMessageHandler<REQ, REQ> {

	@Override
	public REQ onMessage(REQ msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			handleServerSide(msg, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(msg, null);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public abstract void handleClientSide(REQ msg, EntityPlayer player);

	@SideOnly(Side.SERVER)
	public abstract void handleServerSide(REQ msg, EntityPlayer player);

}
