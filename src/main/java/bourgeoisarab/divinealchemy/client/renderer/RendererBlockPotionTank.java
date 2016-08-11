package bourgeoisarab.divinealchemy.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;

public class RendererBlockPotionTank extends TileEntitySpecialRenderer<TEPotionTank> {

	// private ModelPotionTank model = new ModelPotionTank();

	private double renderMinX = 0.0D;
	private double renderMaxX = 1.0D;
	private double renderMinY = 0.2D;
	private double renderMaxY = 0.8D;
	private double renderMinZ = 0.0D;
	private double renderMaxZ = 1.0D;

	@Override
	public void renderTileEntityAt(TEPotionTank tile, double x, double y, double z, float partialTicks, int destroyed) {
		FluidStack fluid = tile.getTankInfo(EnumFacing.UP)[0].fluid;
		if (fluid == null || fluid.amount <= 0) {
			return;
		}

		DivineAlchemy.proxy.getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		// IIcon icon = fluid.getFluid().getIcon(fluid);
		Tessellator t = Tessellator.getInstance();

		// double minU = icon.getInterpolatedU(renderMinX * 16.0D);
		// double maxU = icon.getInterpolatedU(renderMaxX * 16.0D);
		// double minV = icon.getInterpolatedV(renderMinZ * 16.0D);
		// double maxV = icon.getInterpolatedV(renderMaxZ * 16.0D);

		double renderY = y + ((renderMaxY - renderMinY) * ((double) fluid.amount / tile.getCapacity()) + renderMinY);

		// GL11.glDisable(GL11.GL_LIGHTING);
		// t.startDrawingQuads();
		// t.setColorOpaque_I(fluid.getFluid().getColor(fluid));
		// t.addVertexWithUV(x + renderMaxX, renderY, z + renderMaxZ, maxU, maxV);
		// t.addVertexWithUV(x + renderMaxX, renderY, z + renderMinZ, maxU, minV);
		// t.addVertexWithUV(x + renderMinX, renderY, z + renderMinZ, minU, minV);
		// t.addVertexWithUV(x + renderMinX, renderY, z + renderMaxZ, minU, maxV);
		// t.draw();
		// GL11.glEnable(GL11.GL_LIGHTING);
	}

}
