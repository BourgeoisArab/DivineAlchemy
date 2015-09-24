package bourgeoisarab.divinealchemy.common.block.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionVat;
import bourgeoisarab.divinealchemy.init.ModBlocks;

public abstract class BlockMultiBlock extends Block {

	protected BlockMultiBlock(Material material) {
		super(material);
	}

	protected void notifyMultiBlockChange(World world, int x, int y, int z, int meta) {
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			Block block = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
			if (block == ModBlocks.potionVat) {
				TEPotionVat tile = (TEPotionVat) world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
				tile.checkMultiBlock();
				break;
			} else if (block instanceof BlockMultiBlock) {
				world.setBlockMetadataWithNotify(x, y, z, 15, 4);
				int m = world.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
				if (m < 15) {
					((BlockMultiBlock) block).notifyMultiBlockChange(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, m);
				}
			}
		}
		world.setBlockMetadataWithNotify(x, y, z, meta, 4);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		notifyMultiBlockChange(world, x, y, z, world.getBlockMetadata(x, y, z));
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		super.onBlockDestroyedByPlayer(world, x, y, z, meta);
		notifyMultiBlockChange(world, x, y, z, meta);
	}

}
