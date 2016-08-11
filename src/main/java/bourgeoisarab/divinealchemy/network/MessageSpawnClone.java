package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import bourgeoisarab.divinealchemy.common.entity.EntityPlayerClone;
import bourgeoisarab.divinealchemy.utility.Log;

public class MessageSpawnClone extends MessageBase<MessageSpawnClone> {

	public double x;
	public double y;
	public double z;
	public float yaw;
	public float yawHead;
	public String master;

	public MessageSpawnClone() {

	}

	public MessageSpawnClone(EntityPlayerClone clone) {
		x = clone.posX;
		y = clone.posY;
		z = clone.posZ;
		yaw = clone.rotationYaw;
		yawHead = clone.rotationYawHead;
		master = clone.master.getDisplayName().toString();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		yaw = buf.readFloat();
		yawHead = buf.readFloat();
		master = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(yaw);
		buf.writeFloat(yawHead);
		ByteBufUtils.writeUTF8String(buf, master);
	}

	@Override
	public void handleClientSide(MessageSpawnClone msg, EntityPlayer player) {
		Log.warn("MessageSpawnClone handled on server side.");
	}

	@Override
	public void handleServerSide(MessageSpawnClone msg, EntityPlayer player) {
		// World world = DivineAlchemy.proxy.getClientWorld();
		// EntityPlayerClone clone = new EntityPlayerClone(world.getPlayerEntityByName(msg.master));
		// clone.posX = msg.x;
		// clone.posY = msg.y;
		// clone.posZ = msg.z;
		// clone.rotationYaw = msg.yaw;
		// clone.rotationYawHead = msg.yawHead;
		// world.spawnEntityInWorld(clone);
	}

}
