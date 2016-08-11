package bourgeoisarab.divinealchemy.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockMirror extends Block {

	public BlockMirror() {
		super(Material.rock);
		setUnlocalizedName("blockMirror");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	// @Override
	// public boolean renderAsNormalBlock() {
	// return false;
	// }

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB bb, List list, Entity entity) {

	}
}
