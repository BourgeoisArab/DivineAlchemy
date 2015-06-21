package bourgeoisarab.divinealchemy.proxy;

import bourgeoisarab.divinealchemy.client.renderer.RenderEntitySplashPotion;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockPotion;
import bourgeoisarab.divinealchemy.client.renderer.RendererItemBrewingCauldron;
import bourgeoisarab.divinealchemy.common.entity.EntitySpecialCreeper;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.common.entity.RenderSpecialCreeper;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		registerRenderers();
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	private void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TEBrewingCauldron.class, new RendererBlockBrewingCauldron());
		RenderingRegistry.registerBlockHandler(new RendererBlockPotion());
		// ClientRegistry.bindTileEntitySpecialRenderer(TEChannel.class, new RendererBlockChannel());
		// ClientRegistry.bindTileEntitySpecialRenderer(TEItemReceptacle.class, new RendererBlockItemReceptacle());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockBrewingCauldron), new RendererItemBrewingCauldron(new TEBrewingCauldron(), new RendererBlockBrewingCauldron()));
		RenderingRegistry.registerEntityRenderingHandler(EntitySpecialCreeper.class, new RenderSpecialCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplashPotion.class, new RenderEntitySplashPotion());
	}

	@Override
	public Minecraft getClient() {
		return Minecraft.getMinecraft();
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

}
