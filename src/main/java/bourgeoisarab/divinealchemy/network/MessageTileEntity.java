package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.tileentity.TileEntityBaseDA;
import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageTileEntity extends MessagePotisionedBase<MessageTileEntity> {

	public NBTTagCompound tag;

	public MessageTileEntity() {

	}

	public MessageTileEntity(TileEntityBaseDA tile) {
		super(tile.xCoord, tile.yCoord, tile.zCoord);
		tag = new NBTTagCompound();
		tile.writeToNBT(tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void handleClientSide(MessageTileEntity msg, World world, int x, int y, int z, EntityPlayer player) {
		if (world.getChunkProvider().chunkExists(x, z)) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if (tile instanceof TileEntityBaseDA) {
				tile.readFromNBT(msg.tag);
			}
		}
	}

	@Override
	public void handleServerSide(MessageTileEntity msg, World world, int x, int y, int z, EntityPlayer player) {

	}

}
