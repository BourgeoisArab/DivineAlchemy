package bourgeoisarab.divinealchemy.common.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import bourgeoisarab.divinealchemy.network.MessageTileEntity;
import bourgeoisarab.divinealchemy.network.NetworkHandler;

public abstract class TEDivineAlchemy extends TileEntity {

	protected boolean firstRun = true;

	public void sendUpdateToClient() {
		markDirty();
		if (!worldObj.isRemote) {
			NetworkHandler.sendToAll(new MessageTileEntity(this));
		}
		// if (!worldObj.isRemote) {
		// for (EntityPlayerMP player : (List<EntityPlayerMP>) worldObj.playerEntities) {
		// player.playerNetServerHandler.sendPacket(getDescriptionPacket());
		// }
		// }
		// if (!worldObj.isRemote) {
		// // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		// for (EntityPlayerMP player : (List<EntityPlayerMP>) worldObj.playerEntities) {
		// player.playerNetServerHandler.sendPacket(getDescriptionPacket());
		// }
		// }
	}

	@Override
	public Packet getDescriptionPacket() {
		// ByteBuf buf = Unpooled.buffer();
		// buf.writeInt(xCoord);
		// buf.writeInt(yCoord);
		// buf.writeInt(zCoord);
		// writeToPacket(buf);
		// return new FMLProxyPacket(buf, NetworkHandlerDescription.CHANNEL);
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	public void writeToPacket(ByteBuf buf) {

	}

	public void readFromPacket(ByteBuf buf) {

	}

}
