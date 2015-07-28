package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import bourgeoisarab.divinealchemy.network.MessageTileEntity;
import bourgeoisarab.divinealchemy.network.NetworkHandler;

public abstract class TileEntityBaseDA extends TileEntity {

	public void sendUpdateToClient() {
		markDirty();
		if (!worldObj.isRemote) {
			NetworkHandler.sendToAll(new MessageTileEntity(this));
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		Packet packet = super.getDescriptionPacket();
		NBTTagCompound nbtTag = packet != null ? ((S35PacketUpdateTileEntity) packet).func_148857_g() : new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(networkManager, packet);
		readFromNBT(packet.func_148857_g());
	}

}
