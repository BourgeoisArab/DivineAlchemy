package bourgeoisarab.divinealchemy.client.renderer;

import bourgeoisarab.divinealchemy.common.block.BlockPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.utility.ColourHelper;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RendererBlockPotion implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (block != ModBlocks.blockPotion) {
			return false;
		}
		return renderBlockLiquid(world, x, y, z, block, renderer);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return 100;
	}

	public boolean setPotionColour(IBlockAccess world, int x, int y, int z, Block block) {
		if (block == ModBlocks.blockPotion) {
			TEPotion tile = ((BlockPotion) block).getTileEntity(world, x, y, z);
			if (tile == null) {
				return false;
			}
			List<PotionEffect> effects = tile.getEffects();
			if (effects == null) {
				return false;
			}
			if (effects.size() > 0) {
				float[] colours = ColourHelper.getColourFromEffects(effects);
				Tessellator.instance.setColorOpaque_F(colours[0], colours[1], colours[2]);
				return true;
			}
		}
		return false;
	}

	public boolean renderBlockLiquid(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.colorMultiplier(world, x, y, z);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;
		boolean flag = block.shouldSideBeRendered(world, x, y + 1, z, 1);
		boolean flag1 = block.shouldSideBeRendered(world, x, y - 1, z, 0);
		boolean[] aboolean = new boolean[]{block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4),
				block.shouldSideBeRendered(world, x + 1, y, z, 5)};

		if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
			return false;
		} else {
			boolean flag2 = false;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			double d0 = 0.0D;
			double d1 = 1.0D;
			Material material = block.getMaterial();
			int i1 = world.getBlockMetadata(x, y, z);
			double d2 = renderer.getLiquidHeight(x, y, z, material);
			double d3 = renderer.getLiquidHeight(x, y, z + 1, material);
			double d4 = renderer.getLiquidHeight(x + 1, y, z + 1, material);
			double d5 = renderer.getLiquidHeight(x + 1, y, z, material);
			double d6 = 0.0010000000474974513D;
			float f9;
			float f10;
			float f11;

			if (renderer.renderAllFaces || flag) {
				flag2 = true;
				IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 1, i1);
				float f7 = (float) BlockLiquid.getFlowDirection(world, x, y, z, material);

				if (f7 > -999.0F) {
					iicon = renderer.getBlockIconFromSideAndMetadata(block, 2, i1);
				}

				d2 -= d6;
				d3 -= d6;
				d4 -= d6;
				d5 -= d6;
				double d7;
				double d8;
				double d10;
				double d12;
				double d14;
				double d16;
				double d18;
				double d20;

				if (f7 < -999.0F) {
					d7 = iicon.getInterpolatedU(0.0D);
					d14 = iicon.getInterpolatedV(0.0D);
					d8 = d7;
					d16 = iicon.getInterpolatedV(16.0D);
					d10 = iicon.getInterpolatedU(16.0D);
					d18 = d16;
					d12 = d10;
					d20 = d14;
				} else {
					f9 = MathHelper.sin(f7) * 0.25F;
					f10 = MathHelper.cos(f7) * 0.25F;
					f11 = 8.0F;
					d7 = iicon.getInterpolatedU(8.0F + (-f10 - f9) * 16.0F);
					d14 = iicon.getInterpolatedV(8.0F + (-f10 + f9) * 16.0F);
					d8 = iicon.getInterpolatedU(8.0F + (-f10 + f9) * 16.0F);
					d16 = iicon.getInterpolatedV(8.0F + (f10 + f9) * 16.0F);
					d10 = iicon.getInterpolatedU(8.0F + (f10 + f9) * 16.0F);
					d18 = iicon.getInterpolatedV(8.0F + (f10 - f9) * 16.0F);
					d12 = iicon.getInterpolatedU(8.0F + (f10 - f9) * 16.0F);
					d20 = iicon.getInterpolatedV(8.0F + (-f10 - f9) * 16.0F);
				}

				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
				if (!setPotionColour(world, x, y, z, block)) {
					Tessellator.instance.setColorOpaque(ColourHelper.potionColour[0], ColourHelper.potionColour[1], ColourHelper.potionColour[2]);
				}
				tessellator.addVertexWithUV(x + 0, y + d2, z + 0, d7, d14);
				tessellator.addVertexWithUV(x + 0, y + d3, z + 1, d8, d16);
				tessellator.addVertexWithUV(x + 1, y + d4, z + 1, d10, d18);
				tessellator.addVertexWithUV(x + 1, y + d5, z + 0, d12, d20);
				tessellator.addVertexWithUV(x + 0, y + d2, z + 0, d7, d14);
				tessellator.addVertexWithUV(x + 1, y + d5, z + 0, d12, d20);
				tessellator.addVertexWithUV(x + 1, y + d4, z + 1, d10, d18);
				tessellator.addVertexWithUV(x + 0, y + d3, z + 1, d8, d16);
			}

			if (renderer.renderAllFaces || flag1) {
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
				if (!setPotionColour(world, x, y, z, block)) {
					Tessellator.instance.setColorOpaque(ColourHelper.potionColour[0], ColourHelper.potionColour[1], ColourHelper.potionColour[2]);
				}
				renderer.renderFaceYNeg(block, x, y + d6, z, renderer.getBlockIconFromSide(block, 0));
				flag2 = true;
			}

			for (int k1 = 0; k1 < 4; ++k1) {
				int l1 = x;
				int j1 = z;

				if (k1 == 0) {
					j1 = z - 1;
				}

				if (k1 == 1) {
					++j1;
				}

				if (k1 == 2) {
					l1 = x - 1;
				}

				if (k1 == 3) {
					++l1;
				}

				IIcon iicon1 = renderer.getBlockIconFromSideAndMetadata(block, k1 + 2, i1);

				if (renderer.renderAllFaces || aboolean[k1]) {
					double d9;
					double d11;
					double d13;
					double d15;
					double d17;
					double d19;

					if (k1 == 0) {
						d9 = d2;
						d11 = d5;
						d13 = x;
						d17 = x + 1;
						d15 = z + d6;
						d19 = z + d6;
					} else if (k1 == 1) {
						d9 = d4;
						d11 = d3;
						d13 = x + 1;
						d17 = x;
						d15 = z + 1 - d6;
						d19 = z + 1 - d6;
					} else if (k1 == 2) {
						d9 = d3;
						d11 = d2;
						d13 = x + d6;
						d17 = x + d6;
						d15 = z + 1;
						d19 = z;
					} else {
						d9 = d5;
						d11 = d4;
						d13 = x + 1 - d6;
						d17 = x + 1 - d6;
						d15 = z;
						d19 = z + 1;
					}

					flag2 = true;
					float f8 = iicon1.getInterpolatedU(0.0D);
					f9 = iicon1.getInterpolatedU(8.0D);
					f10 = iicon1.getInterpolatedV((1.0D - d9) * 16.0D * 0.5D);
					f11 = iicon1.getInterpolatedV((1.0D - d11) * 16.0D * 0.5D);
					float f12 = iicon1.getInterpolatedV(8.0D);
					tessellator.setBrightness(block.getMixedBrightnessForBlock(world, l1, y, j1));
					float f13 = 1.0F;
					f13 *= k1 < 2 ? f5 : f6;
					if (!setPotionColour(world, x, y, z, block)) {
						Tessellator.instance.setColorOpaque(ColourHelper.potionColour[0], ColourHelper.potionColour[1], ColourHelper.potionColour[2]);
					}
					tessellator.addVertexWithUV(d13, y + d9, d15, f8, f10);
					tessellator.addVertexWithUV(d17, y + d11, d19, f9, f11);
					tessellator.addVertexWithUV(d17, y + 0, d19, f9, f12);
					tessellator.addVertexWithUV(d13, y + 0, d15, f8, f12);
					tessellator.addVertexWithUV(d13, y + 0, d15, f8, f12);
					tessellator.addVertexWithUV(d17, y + 0, d19, f9, f12);
					tessellator.addVertexWithUV(d17, y + d11, d19, f9, f11);
					tessellator.addVertexWithUV(d13, y + d9, d15, f8, f10);
				}
			}

			renderer.renderMinY = d0;
			renderer.renderMaxY = d1;
			return flag2;
		}
	}
	// public float getLiquidHeight(IBlockAccess world, int x, int y, int z, Material material) {
	// int l = 0;
	// float f = 0.0F;
	//
	// for (int i1 = 0; i1 < 4; ++i1) {
	// int j1 = x - (i1 & 1);
	// int k1 = z - (i1 >> 1 & 1);
	//
	// if (world.getBlock(j1, y + 1, k1).getMaterial() == material) {
	// return 1.0F;
	// }
	//
	// Material material1 = world.getBlock(j1, y, k1).getMaterial();
	//
	// if (material1 == material) {
	// int l1 = world.getBlockMetadata(j1, y, k1);
	//
	// if (l1 >= 8 || l1 == 0) {
	// f += BlockLiquid.getLiquidHeightPercent(l1) * 10.0F;
	// l += 10;
	// }
	//
	// f += BlockLiquid.getLiquidHeightPercent(l1);
	// ++l;
	// } else if (!material1.isSolid()) {
	// ++f;
	// ++l;
	// }
	// }
	//
	// return 1.0F - f / l;
	// }

}
