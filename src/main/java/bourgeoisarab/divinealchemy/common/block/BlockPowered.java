package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowered;

public abstract class BlockPowered extends BlockContainer {

	protected BlockPowered(Material material) {
		super(material);
	}

	@Override
	public abstract TEPowered createNewTileEntity(World worldIn, int meta);

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {

	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEPowered) {
			((TEPowered) te).removeFromNetwork();
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion e) {
		super.onBlockDestroyedByExplosion(world, pos, e);
		onBlockDestroyedByPlayer(world, pos, world.getBlockState(pos));
	}
}
