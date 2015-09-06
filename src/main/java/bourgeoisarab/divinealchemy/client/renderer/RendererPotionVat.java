package bourgeoisarab.divinealchemy.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionVat;

public class RendererPotionVat extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		TEPotionVat tile = (TEPotionVat) entity;
		ForgeDirection orientation = tile.orientation;
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.draw();
	}

}
