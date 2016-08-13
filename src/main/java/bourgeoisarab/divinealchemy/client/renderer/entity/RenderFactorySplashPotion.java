package bourgeoisarab.divinealchemy.client.renderer.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.ColourHelper;

public class RenderFactorySplashPotion implements IRenderFactory<EntitySplashPotion> {

	@Override
	public Render<? super EntitySplashPotion> createRenderFor(RenderManager manager) {
		return new RenderEntitsySplashPotion(manager);
	}

	private static class RenderEntitsySplashPotion extends Render<EntitySplashPotion> {

		public RenderEntitsySplashPotion(RenderManager manager) {
			super(manager);
		}

		private final ItemStack stack = new ItemStack(ModItems.bottlePotion, 1, 1);

		@Override
		public void doRender(EntitySplashPotion e, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
			// GL11.glPushMatrix();
			// GL11.glTranslated(x, y, z);
			// GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			EntitySplashPotion entity = e;
			// Tessellator t = Tessellator.instance;
			// NBTHelper.setEffectsForStack(stack, entity.getEffects());
			// IIcon icon1 = ModItems.itemPotionBottle.getIcon(stack, 0);
			// IIcon icon2 = ModItems.itemPotionBottle.getIcon(stack, 1);
			// GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			// GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			// float f1 = icon1.getMinU();
			// float f2 = icon1.getMaxU();
			// float f3 = icon1.getMinV();
			// float f4 = icon1.getMaxV();
			// float f5 = icon1.getMinU();
			// float f6 = icon1.getMaxU();
			// float f7 = icon1.getMinV();
			// float f8 = icon1.getMaxV();
			// float f9 = 1.0F;
			// float f10 = 0.5F;
			// float f11 = 0.25F;
			// t.startDrawingQuads();
			// // t.setNormal(0.0F, 1.0F, 0.0F);
			// t.addVertexWithUV(0.0F - f10, 0.0F - f11, 0.0D, f1, f4);
			// t.addVertexWithUV(f9 - f10, 0.0F - f11, 0.0D, f2, f4);
			// t.addVertexWithUV(f9 - f10, 1.0F - f11, 0.0D, f2, f3);
			// t.addVertexWithUV(0.0F - f10, 1.0F - f11, 0.0D, f1, f3);
			// t.draw();
			// t.startDrawingQuads();
			// // t.setNormal(0.0F, 1.0F, 0.0F);
			// t.addVertexWithUV(0.0F - f10, 0.0F - f11, 0.0D, f5, f8);
			// t.addVertexWithUV(f9 - f10, 0.0F - f11, 0.0D, f6, f8);
			// t.addVertexWithUV(f9 - f10, 1.0F - f11, 0.0D, f6, f7);
			// t.addVertexWithUV(0.0F - f10, 1.0F - f11, 0.0D, f5, f7);
			// t.draw();
			// GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			// GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glTranslatef((float) x, (float) y, (float) z);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(0.5F, 0.5F, 0.5F);

			// bindEntityTexture(entity);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer renderer = tessellator.getWorldRenderer();

			float[] col = ColourHelper.getFloatColours(ColourHelper.separateColours(ModItems.bottlePotion.getColorFromItemStack(stack, 1)));
			GL11.glColor3f(col[0], col[1], col[2]);

			GL11.glPushMatrix();
			// IIcon iicon = stack.getItem().getIcon(stack, 1);
			// draw(tessellator, iicon);
			GL11.glPopMatrix();

			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			// iicon = stack.getItem().getIcon(stack, 0);
			// draw(tessellator, iicon);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();

		}

		@Override
		protected ResourceLocation getEntityTexture(EntitySplashPotion entity) {
			return null;
		}

	}

}
