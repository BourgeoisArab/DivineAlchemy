package bourgeoisarab.divinealchemy.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.item.ItemEnergyStorage;
import bourgeoisarab.divinealchemy.common.tileentity.TEObelisk;
import bourgeoisarab.divinealchemy.init.ModItems;

public class RendererBlockObelisk extends TileEntitySpecialRenderer<TEObelisk> {

	private EntityItem entityCrystalBasic = new EntityItem(null, 0.0D, 0.0D, 0.0D, new ItemStack(ModItems.crystalBasic));
	private EntityItem entityCrystalMedium = new EntityItem(null, 0.0D, 0.0D, 0.0D, new ItemStack(ModItems.crystalMedium));
	private EntityItem entityCrystalBig = new EntityItem(null, 0.0D, 0.0D, 0.0D, new ItemStack(ModItems.crystalBig));

	@Override
	public void renderTileEntityAt(TEObelisk te, double x, double y, double z, float partialTicks, int destroyStage) {
		TEObelisk tile = te;
		ItemStack stack = tile.getStoredItem();
		if (stack == null) {
			return;
		}
		GL11.glPushMatrix();

		GlStateManager.translate(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		renderItem(te.getWorld(), stack, partialTicks);

		GL11.glPopMatrix();
	}

	private void renderItem(World world, ItemStack stack, float partialTicks) {
		RenderItem itemRenderer = DivineAlchemy.proxy.getClient().getRenderItem();
		if (stack != null) {
			EntityItem entity = entityCrystalBasic;
			if (stack.getItem() == ModItems.crystalMedium) {
				entity = entityCrystalMedium;
			} else if (stack.getItem() == ModItems.crystalBig) {
				entity = entityCrystalBig;
			}

			// Updates crystal charge
			ItemEnergyStorage item = (ItemEnergyStorage) entity.getEntityItem().getItem();
			item.setEnergy(entity.getEntityItem(), item.getEnergy(stack));

			entity.hoverStart = 0.0F;
			GlStateManager.translate(0.5, 0.5, 0.5);

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
}
