package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public abstract class MessagePotisionedBase<REQ extends IMessage> extends MessageBase<REQ> {

	public int x;
	public int y;
	public int z;

	public MessagePotisionedBase() {

	}

	public MessagePotisionedBase(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	@Override
	public REQ onMessage(REQ msg, MessageContext ctx) {
		EntityPlayer player;
		if (ctx.side == Side.SERVER) {
			handleServerSide(msg, ctx.getServerHandler().playerEntity.worldObj, ((MessagePotisionedBase) msg).x, ((MessagePotisionedBase) msg).y, ((MessagePotisionedBase) msg).z, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(msg, DivineAlchemy.proxy.getClientWorld(), ((MessagePotisionedBase) msg).x, ((MessagePotisionedBase) msg).y, ((MessagePotisionedBase) msg).z, DivineAlchemy.proxy.getClientPlayer());
		}
		return null;
	}

	public abstract void handleClientSide(REQ msg, World world, int x, int y, int z, EntityPlayer player);

	public abstract void handleServerSide(REQ msg, World world, int x, int y, int z, EntityPlayer player);

	@Override
	public void handleClientSide(REQ msg, EntityPlayer player) {

	}

	@Override
	public void handleServerSide(REQ msg, EntityPlayer player) {

	}

}
