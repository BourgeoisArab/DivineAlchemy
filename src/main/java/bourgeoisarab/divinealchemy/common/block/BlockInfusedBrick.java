package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.block.multiblock.IMultiBlock;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;

public class BlockInfusedBrick extends Block implements IMultiBlock {

	public BlockInfusedBrick() {
		super(Material.rock);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setBlockName("blockInfusedBrick");
		setBlockTextureName(Ref.MODID + ":brick_infused");
		setHardness(10.0F);
		setResistance(1750.0F);
	}

	@Override
	public void notifyMultiblockChange(World world, int x, int y, int z) {
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			Block block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			if (block instanceof IMultiBlock) {
				// ((IMultiBlock) block).notifyMultiblockChange(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				// if (block == ModBlocks.blockPotionVat) {
				// break;
				// }
			}
		}
	}

}
