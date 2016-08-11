package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowerProvider;

public abstract class BlockPowerProvider extends BlockContainer {

	protected BlockPowerProvider(Material material) {
		super(material);
	}

	@Override
	public abstract TEPowerProvider createNewTileEntity(World worldIn, int meta);

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TEPowerProvider) {
			((TEPowerProvider) tile).removeFromNetwork();
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion e) {
		super.onBlockDestroyedByExplosion(world, pos, e);
		onBlockDestroyedByPlayer(world, pos, world.getBlockState(pos));
	}
}
