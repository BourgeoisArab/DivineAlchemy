package bourgeoisarab.divinealchemy.network;

import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {

	public static SimpleNetworkWrapper INSTANCE;

	public static void init() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Ref.MODID);
		INSTANCE.registerMessage(MessagePotionEffect.class, MessagePotionEffect.class, 0, Side.CLIENT);
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
