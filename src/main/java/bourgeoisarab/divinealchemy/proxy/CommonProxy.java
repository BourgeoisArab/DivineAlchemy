package bourgeoisarab.divinealchemy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class CommonProxy {

	public abstract void preInit();

	public abstract void init();

	public abstract void postInit();

	public abstract Minecraft getClient();

	public abstract EntityPlayer getClientPlayer();

	public abstract World getClientWorld();

}
