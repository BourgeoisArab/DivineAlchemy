package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import bourgeoisarab.divinealchemy.common.tileentity.TEDivineAlchemy;
import bourgeoisarab.divinealchemy.utility.Log;

public class MessageTileEntity extends MessagePotisionedBase<MessageTileEntity> {

	public NBTTagCompound tag;

	public MessageTileEntity() {

	}

	public MessageTileEntity(TEDivineAlchemy tile) {
		super(tile.getPos());
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
	public void handleClientSide(MessageTileEntity msg, World world, BlockPos pos, EntityPlayer player) {
		if (world != null) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TEDivineAlchemy) {
				tile.readFromNBT(msg.tag);
			}
		}
	}

	@Override
	public void handleServerSide(MessageTileEntity msg, World world, BlockPos pos, EntityPlayer player) {
		Log.warn("TileEntity update message should not be received on the server.");
	}

}
