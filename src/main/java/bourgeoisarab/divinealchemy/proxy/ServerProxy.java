package bourgeoisarab.divinealchemy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ServerProxy extends CommonProxy {

	@Override
	public void preInit() {

	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public Minecraft getClient() {
		return null;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

	@Override
	public World getClientWorld() {
		return null;
	}
}
