package bourgeoisarab.divinealchemy.common.event;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DARenderEventHooks {

	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		float partialTicks = event.partialTicks;
		RenderGlobal render = event.context;
	}

	// @SubscribeEvent
	public void renderEntity(RenderLivingEvent event) {
		// EntityLivingBase entity = event.entity;
		// if (entity.getActivePotionEffect(ModPotion.potionFiendFyre) != null) {
		// Tessellator t = Tessellator.instance;
		// DivineAlchemy.proxy.getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		// IIcon icon = Blocks.fire.getIcon(0, 0);
		// t.startDrawingQuads();
		// t.setColorOpaque_F(1.0F, 0.5F, 0.5F);
		// t.addVertexWithUV(entity.boundingBox.minX, entity.boundingBox.minY, entity.boundingBox.minZ, icon.getMinU(), icon.getMinV());
		// t.addVertexWithUV(entity.boundingBox.maxX, entity.boundingBox.minY, entity.boundingBox.minZ, icon.getMinU(), icon.getMinV());
		// t.addVertexWithUV(entity.boundingBox.minX, entity.boundingBox.maxY, entity.boundingBox.maxZ, icon.getMinU(), icon.getMinV());
		// t.addVertexWithUV(entity.boundingBox.maxX, entity.boundingBox.maxY, entity.boundingBox.maxZ, icon.getMinU(), icon.getMinV());
		// t.draw();
		// }
	}

}
