package bourgeoisarab.divinealchemy.common.event;

import java.util.List;

import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldEvent;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DARenderEventHooks {

	@SubscribeEvent
	public void renderLiving(RenderWorldEvent.Post event) {
		World world = event.renderer.worldObj;
		List<EntitySpellParticleFX> entities = world.getEntitiesWithinAABB(EntitySpellParticleFX.class, event.renderer.rendererBoundingBox);
		// Log.info(entities);
		DivineAlchemy.proxy.getClient().effectRenderer.clearEffects(DivineAlchemy.proxy.getClientWorld());
	}

}
