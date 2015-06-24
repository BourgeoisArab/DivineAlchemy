package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageCancelFlight extends MessageBase<MessageCancelFlight> {

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	@Override
	public void handleClientSide(MessageCancelFlight msg, EntityPlayer player) {
		player.capabilities.allowFlying = false;
		player.capabilities.isFlying = false;
	}

	@Override
	public void handleServerSide(MessageCancelFlight msg, EntityPlayer player) {

	}

}
