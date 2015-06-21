package bourgeoisarab.divinealchemy.client.renderer;

import bourgeoisarab.divinealchemy.client.renderer.model.ModelItemReceptacle;
import bourgeoisarab.divinealchemy.common.tileentity.TEItemReceptacle;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RendererBlockItemReceptacle extends TileEntitySpecialRenderer {

	private ModelItemReceptacle model = new ModelItemReceptacle();
	private final RenderItem renderItem;

	public RendererBlockItemReceptacle() {
		renderItem = new RenderItem() {
			@Override
			public boolean shouldBob() {
				return true;
			}
		};
		renderItem.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		model.render((TEItemReceptacle) entity, x, y, z);
		if (((TEItemReceptacle) entity).getItem() != null) {
			renderGhostItem((TEItemReceptacle) entity, x, y, z);
		}
	}

	public void renderGhostItem(TEItemReceptacle entity, double x, double y, double z) {
		GL11.glPushMatrix();
		// float rotation = (System.currentTimeMillis() % 4000) * 100 / 9;
		float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
		EntityItem item = new EntityItem(entity.getWorldObj());
		item.setEntityItemStack(entity.getItem());
		item.hoverStart = 0.0F;
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		renderItem.doRender(item, 0, 0, 0, 0, 0);
		GL11.glPopMatrix();
	}
}
