package bourgeoisarab.divinealchemy.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import bourgeoisarab.divinealchemy.reference.Ref;

public class NetworkHandler {

	public static SimpleNetworkWrapper INSTANCE;

	public static void init() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Ref.MODID);
		INSTANCE.registerMessage(MessageTileEntity.class, MessageTileEntity.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageRemoveEffect.class, MessageRemoveEffect.class, 1, Side.CLIENT);
		// INSTANCE.registerMessage(MessageSpawnClone.class, MessageSpawnClone.class, 2, Side.SERVER);
	}

	public static void sendToServer(IMessage msg) {
		INSTANCE.sendToServer(msg);
	}

	public static void sendTo(IMessage msg, EntityPlayerMP player) {
		INSTANCE.sendTo(msg, player);
	}

	public static void sendToAll(IMessage msg) {
		INSTANCE.sendToAll(msg);
	}

	public static void sendToAllAround(IMessage msg, TargetPoint point) {
		INSTANCE.sendToAllAround(msg, point);
	}

	public static void sendToAll(IMessage msg, int dimID) {
		INSTANCE.sendToDimension(msg, dimID);
	}

}
