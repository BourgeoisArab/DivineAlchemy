package bourgeoisarab.divinealchemy.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.Log;

public class RendererBlockBrewingCauldron extends TileEntitySpecialRenderer<TEBrewingCauldron> {

	private static final ResourceLocation WATER = new ResourceLocation("minecraft:textures/blocks/water_still.png");
	private static final ResourceLocation LAVA = new ResourceLocation("minecraft:textures/blocks/lava_still.png");

	public RendererBlockBrewingCauldron() {

	}

	@Override
	public void renderTileEntityAt(TEBrewingCauldron tile, double x, double y, double z, float partialTicks, int destroyStage) {
		int level = tile.tank.getFluidAmount();
		if (level <= 0) {
			return;
		}

		FluidStack fluid = tile.tank.getFluid();

		GL11.glPushMatrix();

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();

		// TextureAtlasSprite sprite;
		// if (fluid.getFluid() == FluidRegistry.WATER) {
		// texture = ;
		// } else if (fluid.getFluid() == FluidRegistry.LAVA) {
		// bindTexture(LAVA);
		// } else {
		// bindTexture(fluid.getFluid().getStill(fluid));
		// }

		// TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
		if (sprite == null) {
			Log.info("sprite is null");
			GL11.glPopMatrix();
			return;
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);

		float[] colour;
		// if (fluid.getFluid() == ModFluids.potion) {
		// GL11.glEnable(GL11.GL_BLEND);
		// if (tile.getEffects().size() > 0) {
		// colour = ColourHelper.getColourFromEffects(tile.getEffects().getEffects(), tile.getColouring());
		// } else {
		// colour = ColourHelper.getColourFromIngredients(tile.getIngredients().getIngredients(), tile.getColouring());
		// }
		// } else
		if (fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == FluidRegistry.LAVA) {
			colour = new float[]{1.0F, 1.0F, 1.0F};
		} else {
			colour = ColourHelper.getFloatColours(ColourHelper.separateColours(fluid.getFluid().getColor(fluid)));
		}

		GlStateManager.color(colour[0], colour[1], colour[2]);

		GlStateManager.translate(x, y, z);

		float fluidHeight = (float) (0.3125 + level / (1.6 * tile.tank.getCapacity()));

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(1.0, fluidHeight, 1.0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		renderer.pos(1.0, fluidHeight, 0.0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		renderer.pos(0.0, fluidHeight, 0.0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		renderer.pos(0.0, fluidHeight, 1.0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		tessellator.draw();

		GL11.glPopMatrix();
	}
}
