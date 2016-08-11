package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEDivineAlchemy;
import bourgeoisarab.divinealchemy.reference.Ref;

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
		int x = buf.readInt(),
		y = buf.readInt(),
		z = buf.readInt();
		TileEntity entity = DivineAlchemy.proxy.getClientWorld().getTileEntity(new BlockPos(x, y, z));
		if (entity instanceof TEDivineAlchemy) {
			((TEDivineAlchemy) entity).readFromPacket(buf);
		}
	}

}
