package bourgeoisarab.divinealchemy.client.renderer.entity;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import bourgeoisarab.divinealchemy.common.entity.EntityAmuletSpell;

public class RendererFactoryAmuletSpell implements IRenderFactory<EntityAmuletSpell> {

	@Override
	public Render<? super EntityAmuletSpell> createRenderFor(RenderManager manager) {
		return new RendererEntistyAmuletSpell(manager);
	}

	private static class RendererEntistyAmuletSpell extends Render<EntityAmuletSpell> {

		public RendererEntistyAmuletSpell(RenderManager renderManager) {
			super(renderManager);
		}

		@Override
		public void doRender(EntityAmuletSpell entity, double x, double y, double z, float entityYaw, float partialTicks) {
			Random rand = entity.worldObj.rand;
			for (int i = 0; i < 4; i++) {
				EntityFX particle = new EntitySpellParticleFX.WitchFactory().getEntityFX(17, entity.worldObj, entity.posX + rand.nextFloat() / 4, entity.posY + rand.nextFloat() / 4, entity.posZ + rand.nextFloat() / 4, 0, 0,
						0);
				particle.setRBGColorF(255, 0, 0);
				// DivineAlchemy.proxy.getClient().renderGlobal.spawnParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), false, entity.posX + rand.nextFloat() / 4, entity.posY + rand.nextFloat() / 4,
				// entity.posZ + rand.nextFloat() / 4, 0, 0, 0);
			}
		}

		@Override
		protected ResourceLocation getEntityTexture(EntityAmuletSpell entity) {
			return null;
		}

	}

}
