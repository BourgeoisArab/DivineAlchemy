package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import bourgeoisarab.divinealchemy.DivineAlchemy;

public abstract class MessagePotisionedBase<REQ extends IMessage> extends MessageBase<REQ> {

	public BlockPos pos;

	public MessagePotisionedBase() {

	}

	public MessagePotisionedBase(int x, int y, int z) {
		this.pos = new BlockPos(x, y, z);
	}
	
	public MessagePotisionedBase(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt(),
		y = buf.readInt(),
		z = buf.readInt();
		this.pos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	@Override
	public REQ onMessage(REQ msg, MessageContext ctx) {
		EntityPlayer player;
		if (ctx.side == Side.SERVER) {
			handleServerSide(msg, ctx.getServerHandler().playerEntity.worldObj, ((MessagePotisionedBase) msg).pos, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(msg, DivineAlchemy.proxy.getClientWorld(), ((MessagePotisionedBase) msg).pos, DivineAlchemy.proxy.getClientPlayer());
		}
		return null;
	}

	public abstract void handleClientSide(REQ msg, World world, BlockPos pos, EntityPlayer player);

	public abstract void handleServerSide(REQ msg, World world, BlockPos pos, EntityPlayer player);

	@Override
	public void handleClientSide(REQ msg, EntityPlayer player) {

	}

	@Override
	public void handleServerSide(REQ msg, EntityPlayer player) {

	}

}
