package bourgeoisarab.divinealchemy.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPedestal;
import bourgeoisarab.divinealchemy.init.ModItems;

public class RendererBlockPedestal extends TileEntitySpecialRenderer<TEPedestal> {

	public static final EntityItem entity = new EntityItem(null, 0, 0, 0, new ItemStack(ModItems.pedestalCrystal));

	@Override
	public void renderTileEntityAt(TEPedestal tile, double x, double y, double z, float partialTicks, int destroyStage) {
		ItemStack stack = tile.getStackInSlot(0);
		if (stack != null) {
			GL11.glPushMatrix();

			GlStateManager.translate(x, y, z);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			renderItem(stack);

			GL11.glPopMatrix();
		}
	}

	private void renderItem(ItemStack stack) {
		entity.getEntityItem().readFromNBT(stack.writeToNBT(new NBTTagCompound()));

		RenderItem itemRenderer = DivineAlchemy.proxy.getClient().getRenderItem();
		entity.hoverStart = 0.0F;
		GlStateManager.translate(0.5, 1.125, 0.5);

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

		GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();

		itemRenderer.renderItem(entity.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);

		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

}
