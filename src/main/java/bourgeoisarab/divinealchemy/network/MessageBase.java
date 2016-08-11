package bourgeoisarab.divinealchemy.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import bourgeoisarab.divinealchemy.DivineAlchemy;

public abstract class MessageBase<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {

	@Override
	public REQ onMessage(REQ msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			handleServerSide(msg, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(msg, DivineAlchemy.proxy.getClientPlayer());
		}
		return null;
	}

	public abstract void handleClientSide(REQ msg, EntityPlayer player);

	public abstract void handleServerSide(REQ msg, EntityPlayer player);

}
