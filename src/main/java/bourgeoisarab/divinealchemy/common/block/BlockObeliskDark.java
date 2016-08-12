package bourgeoisarab.divinealchemy.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEObeliskDark;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowerProvider;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class BlockObeliskDark extends BlockPowerProvider {

	public static PropertyBool PROPERTY_TYPE = PropertyBool.create("receptacle");

	public BlockObeliskDark() {
		super(Material.rock);
		setUnlocalizedName("obeliskDark");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{PROPERTY_TYPE});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PROPERTY_TYPE, meta % 2 == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY_TYPE) ? 1 : 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble() - 0.25D, pos.getZ() + rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(),
				(rand.nextDouble() - 0.5D) * 2.0D);
	}

	@Override
	public TEPowerProvider createNewTileEntity(World world, int meta) {
		if (meta % 2 == 1) {
			return new TEObeliskDark();
		}
		return null;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		if (!state.getValue(PROPERTY_TYPE) && world.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isReplaceable(world, pos.offset(EnumFacing.UP))) {
			world.setBlockState(pos.offset(EnumFacing.UP, 3), state.withProperty(PROPERTY_TYPE, true));
			world.setBlockState(pos.offset(EnumFacing.UP, 2), state);
			world.setBlockState(pos.offset(EnumFacing.UP), state);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && world.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isReplaceable(world, pos.offset(EnumFacing.UP))
				&& world.getBlockState(pos.offset(EnumFacing.UP, 2)).getBlock().isReplaceable(world, pos.offset(EnumFacing.UP, 2))
				&& world.getBlockState(pos.offset(EnumFacing.UP, 3)).getBlock().isReplaceable(world, pos.offset(EnumFacing.UP, 3));
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		if (state.getBlock() == this) {
			if (state.getValue(PROPERTY_TYPE)) {
				world.setBlockToAir(pos);
				world.setBlockToAir(pos.offset(EnumFacing.DOWN));
				world.setBlockToAir(pos.offset(EnumFacing.DOWN, 2));
				world.setBlockToAir(pos.offset(EnumFacing.DOWN, 3));
			} else {
				IBlockState state1 = world.getBlockState(pos.offset(EnumFacing.UP));
				if (state1.getBlock() == this && state1.getValue(PROPERTY_TYPE)) {
					onBlockDestroyedByPlayer(world, pos.offset(EnumFacing.UP), state1);
					return;
				}
				state1 = world.getBlockState(pos.offset(EnumFacing.UP, 2));
				if (state1.getBlock() == this && state1.getValue(PROPERTY_TYPE)) {
					onBlockDestroyedByPlayer(world, pos.offset(EnumFacing.UP, 2), state1);
					return;
				}
				state1 = world.getBlockState(pos.offset(EnumFacing.UP, 3));
				if (state1.getBlock() == this && state1.getValue(PROPERTY_TYPE)) {
					onBlockDestroyedByPlayer(world, pos.offset(EnumFacing.UP, 3), state1);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 1));
	}

}
