package bourgeoisarab.divinealchemy.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.client.renderer.model.ModelSpecialCreeper;
import bourgeoisarab.divinealchemy.common.entity.EntitySpecialCreeper;

public class RenderSpecialCreeper extends RenderLiving {

	private static final ResourceLocation armoredCreeperTextures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
	/** The creeper model. */
	private ModelBase creeperModel = new ModelCreeper(2.0F);

	public RenderSpecialCreeper() {
		super(new ModelSpecialCreeper(), 0.5F);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntitySpecialCreeper entity, float tickTime) {
		float f1 = entity.getCreeperFlashIntensity(tickTime);
		float f2 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;

		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f1 > 1.0F) {
			f1 = 1.0F;
		}

		f1 *= f1;
		f1 *= f1;
		float f3 = (1.0F + f1 * 0.4F) * f2;
		float f4 = (1.0F + f1 * 0.1F) / f2;
		GL11.glScalef(f3, f4, f3);
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
	 */
	protected int getColorMultiplier(EntitySpecialCreeper entity, float lightLevel, float tickTime) {
		float f2 = entity.getCreeperFlashIntensity(tickTime);

		if ((int) (f2 * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i = (int) (f2 * 0.2F * 255.0F);

			if (i < 0) {
				i = 0;
			}

			if (i > 255) {
				i = 255;
			}

			short short1 = 255;
			short short2 = 255;
			short short3 = 255;
			return i << 24 | short1 << 16 | short2 << 8 | short3;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntitySpecialCreeper entity, int p_77032_2_, float p_77032_3_) {
		if (entity.getPowered()) {
			if (entity.isInvisible()) {
				GL11.glDepthMask(false);
			} else {
				GL11.glDepthMask(true);
			}

			if (p_77032_2_ == 1) {
				float f1 = entity.ticksExisted + p_77032_3_;
				bindTexture(armoredCreeperTextures);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				float f2 = f1 * 0.01F;
				float f3 = f1 * 0.01F;
				GL11.glTranslatef(f2, f3, 0.0F);
				setRenderPassModel(creeperModel);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_BLEND);
				float f4 = 0.5F;
				GL11.glColor4f(f4, f4, f4, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				return 1;
			}

			if (p_77032_2_ == 2) {
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}

		return -1;
	}

	protected int inheritRenderPass(EntitySpecialCreeper entity, int p_77035_2_, float p_77035_3_) {
		return -1;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntitySpecialCreeper entity) {
		return creeperTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float tickTime) {
		this.preRenderCallback((EntitySpecialCreeper) entity, tickTime);
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
	 */
	@Override
	protected int getColorMultiplier(EntityLivingBase entity, float lightLevel, float tickTime) {
		return this.getColorMultiplier((EntitySpecialCreeper) entity, lightLevel, tickTime);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase entity, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntitySpecialCreeper) entity, p_77032_2_, p_77032_3_);
	}

	@Override
	protected int inheritRenderPass(EntityLivingBase entity, int p_77035_2_, float p_77035_3_) {
		return this.inheritRenderPass((EntitySpecialCreeper) entity, p_77035_2_, p_77035_3_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntitySpecialCreeper) entity);
	}
}
