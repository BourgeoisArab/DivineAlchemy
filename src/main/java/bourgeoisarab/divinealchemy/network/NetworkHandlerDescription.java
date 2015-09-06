package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.tileentity.TileEntity;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TileEntityBaseDA;
import bourgeoisarab.divinealchemy.reference.Ref;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class NetworkHandlerDescription extends SimpleChannelInboundHandler<FMLProxyPacket> {

	public static final String CHANNEL = Ref.MODID + "Description";

	static {
		NetworkRegistry.INSTANCE.newChannel(CHANNEL, new NetworkHandlerDescription());
	}

	public static void init() {
		// just here to call static ^^
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
		ByteBuf buf = msg.payload();
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		TileEntity entity = DivineAlchemy.proxy.getClientWorld().getTileEntity(x, y, z);
		if (entity instanceof TileEntityBaseDA) {
			((TileEntityBaseDA) entity).readFromPacket(buf);
		}
	}

}
