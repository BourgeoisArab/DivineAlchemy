package bourgeoisarab.divinealchemy.client.renderer;

import bourgeoisarab.divinealchemy.client.renderer.model.ModelBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.client.FMLClientHandler;

public class RendererBlockBrewingCauldron extends TileEntitySpecialRenderer {

	private ModelBrewingCauldron model = new ModelBrewingCauldron();
	// private final RenderItem customRenderItem;
	private IIcon icon;

	private double renderMinX = 0.0;
	private double renderMaxX = 1.0;
	private double renderMinY = 0.0;
	private double renderMaxY = 1.0;
	private double renderMinZ = 0.0;
	private double renderMaxZ = 1.0;

	public RendererBlockBrewingCauldron() {
		// customRenderItem = new RenderItem();
		// customRenderItem.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		TEBrewingCauldron tile = (TEBrewingCauldron) tileEntity;
		model.render(tile, x, y, z);
		int level = tile.getFluidAmount();

		if (level > 0) {

			FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			boolean doColour = false;
			if (tile.getFluid().getFluid() == FluidRegistry.WATER) {
				icon = BlockLiquid.getLiquidIcon("water_still");
			} else {
				icon = tile.getFluid().getFluid().getBlock().getIcon(0, 0);
				if (tile.getFluid().getFluid() == ModFluids.fluidPotion) {
					doColour = true;
				}
			}
			renderFluidLevel(tile, x, y - 0.6875 + level / (1.6 * ((TEBrewingCauldron) tileEntity).getCapacity()), z, doColour);
		}
	}

	public void renderFluidLevel(TEBrewingCauldron tile, double x, double y, double z, boolean doColour) {
		Tessellator t = Tessellator.instance;
		double d3 = icon.getInterpolatedU(renderMinX * 16.0D);
		double d4 = icon.getInterpolatedU(renderMaxX * 16.0D);
		double d5 = icon.getInterpolatedV(renderMinZ * 16.0D);
		double d6 = icon.getInterpolatedV(renderMaxZ * 16.0D);

		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		double d11 = x + renderMinX;
		double d12 = x + renderMaxX;
		double d13 = y + renderMaxY;
		double d14 = z + renderMinZ;
		double d15 = z + renderMaxZ;

		t.startDrawingQuads();
		if (doColour) {
			float[] colour = ColourHelper.getColourFromIngredients(tile.getIngredients());
			t.setColorOpaque_F(colour[0], colour[1], colour[2]);
		} else {
			t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		}

		t.addVertexWithUV(d12, d13, d15, d4, d6);
		t.addVertexWithUV(d12, d13, d14, d7, d9);
		t.addVertexWithUV(d11, d13, d14, d3, d5);
		t.addVertexWithUV(d11, d13, d15, d8, d10);
		t.draw();
	}
}
