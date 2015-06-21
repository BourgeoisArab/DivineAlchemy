package bourgeoisarab.divinealchemy.client.renderer;

import bourgeoisarab.divinealchemy.client.renderer.model.ModelChannel;
import bourgeoisarab.divinealchemy.common.tileentity.TEChannel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RendererBlockChannel extends TileEntitySpecialRenderer {

	private ModelChannel model = new ModelChannel();

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		model.render((TEChannel) entity, x, y, z);
	}

}
