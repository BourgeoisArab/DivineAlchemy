package bourgeoisarab.divinealchemy.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.item.ItemEnergyStorage;
import bourgeoisarab.divinealchemy.common.tileentity.TEObelisk;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowerProvider;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModBlocks;

public class BlockObelisk extends BlockPowerProvider {

	public static final PropertyInteger PROPERTY_TIER = PropertyInteger.create("tier", 0, 2);
	public static final PropertyBool PROPERTY_TYPE = PropertyBool.create("receptacle");
	public static final PropertyBool PROPERTY_MULTIBLOCK = PropertyBool.create("multiblock");

	public BlockObelisk() {
		super(Material.rock);
		setUnlocalizedName("obelisk");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setHardness(1.0F);
		setResistance(2.5F);
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
		return new BlockState(this, new IProperty[]{PROPERTY_TIER, PROPERTY_TYPE, PROPERTY_MULTIBLOCK});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PROPERTY_TYPE, (meta & 1) == 1).withProperty(PROPERTY_MULTIBLOCK, (meta & 2) == 2).withProperty(PROPERTY_TIER, meta >> 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(PROPERTY_TIER) << 2) + (state.getValue(PROPERTY_MULTIBLOCK) ? 2 : 0) + (state.getValue(PROPERTY_TYPE) ? 1 : 0);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TEPowerProvider createNewTileEntity(World world, int meta) {
		if ((meta & 1) == 1) {
			return new TEObelisk();
		}
		return null;
	}

	/**
	 * @param world
	 * @param pos of the block itself
	 * @return
	 */
	public boolean checkMultiblock(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		IBlockState state1;
		IBlockState state2;
		int tier = state.getValue(PROPERTY_TIER);
		if (state.getValue(PROPERTY_TYPE) && !state.getValue(PROPERTY_MULTIBLOCK)) {
			state1 = world.getBlockState(pos.offset(EnumFacing.DOWN));
			state2 = world.getBlockState(pos.offset(EnumFacing.DOWN, 2));
			if (state1.getBlock() == this && state2.getBlock() == this) {
				return !state1.getValue(PROPERTY_TYPE) && !state1.getValue(PROPERTY_TYPE) && state1.getValue(PROPERTY_TIER) == tier && state2.getValue(PROPERTY_TIER) == tier;
			}
		} else {
			state = world.getBlockState(pos.offset(EnumFacing.UP));
			if (state.getBlock() == this && state.getValue(PROPERTY_TYPE) && !state.getValue(PROPERTY_MULTIBLOCK)) {
				return checkMultiblock(world, pos.offset(EnumFacing.UP));
			}
			state = world.getBlockState(pos.offset(EnumFacing.UP, 2));
			if (state.getBlock() == this && state.getValue(PROPERTY_TYPE) && !state.getValue(PROPERTY_MULTIBLOCK)) {
				return checkMultiblock(world, pos.offset(EnumFacing.UP, 2));
			}
		}
		return false;
	}

	/**
	 * @param world
	 * @param pos of the receptacle block
	 */
	public void makeObelisk(World world, BlockPos pos) {
		int tier = world.getBlockState(pos).getValue(PROPERTY_TIER);
		IBlockState newState = getDefaultState().withProperty(PROPERTY_TYPE, false).withProperty(PROPERTY_MULTIBLOCK, true).withProperty(PROPERTY_TIER, tier);
		world.setBlockState(pos, newState.withProperty(PROPERTY_TYPE, true), 3);
		world.setBlockState(pos.offset(EnumFacing.DOWN), newState, 3);
		world.setBlockState(pos.offset(EnumFacing.DOWN, 2), newState, 3);
	}

	public void tryToMakeObelisk(World world, BlockPos pos, IBlockState state) {
		if (checkMultiblock(world, pos)) {
			BlockPos receptacle = getReceptacleBlock(world, pos, state);
			if (receptacle != null) {
				makeObelisk(world, receptacle);
			}
		}
	}

	private BlockPos getReceptacleBlock(IBlockAccess world, BlockPos pos, IBlockState state) {
		if (state.getValue(PROPERTY_TYPE)) {
			return pos;
		}
		for (int i = 1; i < 3; i++) {
			BlockPos newPos = pos.offset(EnumFacing.UP, i);
			IBlockState newState = world.getBlockState(newPos);
			if (newState.getBlock() == ModBlocks.obelisk && newState.getValue(PROPERTY_TYPE)) {
				return newPos;
			}
		}
		return null;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		if (!state.getValue(PROPERTY_MULTIBLOCK)) {
			tryToMakeObelisk(world, pos, state);
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (!state.getValue(PROPERTY_MULTIBLOCK)) {
			return super.getDrops(world, pos, state, fortune);
		}
		List<ItemStack> list = new ArrayList<ItemStack>();
		IBlockState newState = state.withProperty(PROPERTY_MULTIBLOCK, false).withProperty(PROPERTY_TYPE, false);
		list.add(new ItemStack(ModBlocks.obelisk, 1, getMetaFromState(newState.withProperty(PROPERTY_TYPE, true))));
		list.add(new ItemStack(ModBlocks.obelisk, 2, getMetaFromState(newState)));
		return list;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(world, pos, state, player);
		if (!world.isRemote && state.getValue(PROPERTY_MULTIBLOCK)) {
			if (state.getValue(PROPERTY_TYPE)) {
				ItemStack stack = ((TEObelisk) world.getTileEntity(pos)).getStoredItem();
				if (stack != null) {
					world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			} else if (world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() == this) {
				onBlockHarvested(world, pos.offset(EnumFacing.UP), world.getBlockState(pos.offset(EnumFacing.UP)), player);
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		if (state.getBlock() == this && state.getValue(PROPERTY_MULTIBLOCK)) {
			if (state.getValue(PROPERTY_TYPE)) {
				world.setBlockToAir(pos.offset(EnumFacing.DOWN));
				world.setBlockToAir(pos.offset(EnumFacing.DOWN, 2));
			} else {
				IBlockState state1 = world.getBlockState(pos.offset(EnumFacing.UP));
				if (state1.getBlock() == ModBlocks.obelisk && state1.getValue(PROPERTY_TYPE)) {
					world.setBlockToAir(pos.offset(EnumFacing.UP));
					world.setBlockToAir(pos.offset(EnumFacing.DOWN));
					return;
				}
				state1 = world.getBlockState(pos.offset(EnumFacing.UP, 2));
				if (state1.getBlock() == ModBlocks.obelisk && state1.getValue(PROPERTY_TYPE)) {
					world.setBlockToAir(pos.offset(EnumFacing.UP));
					world.setBlockToAir(pos.offset(EnumFacing.UP, 2));
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!state.getValue(PROPERTY_MULTIBLOCK)) {
			return false;
		}
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack == null || stack.getItem() instanceof ItemEnergyStorage) {
			TEObelisk tile = (TEObelisk) world.getTileEntity(pos);
			if (tile == null) {
				return world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() == this ? onBlockActivated(world, pos.offset(EnumFacing.UP), state, player, side, hitX, hitY, hitZ) : false;
			}
			if (tile.getStoredItem() == null && stack != null) {
				tile.setStoredItem(stack);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				return true;
			} else if (tile.getStoredItem() != null && stack == null) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getStoredItem());
				tile.setStoredItem(null);
				return true;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 4));
		list.add(new ItemStack(item, 1, 5));
		list.add(new ItemStack(item, 1, 8));
		list.add(new ItemStack(item, 1, 9));
	}

}
