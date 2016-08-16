package bourgeoisarab.divinealchemy.common.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.tileentity.TEObelisk;
import bourgeoisarab.divinealchemy.common.tileentity.TEPedestal;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionBrewer;
import bourgeoisarab.divinealchemy.utility.Log;

public class BrewingSetup {

	public static final int searchRange = 7; // 8 including cauldron block

	protected List<BlockPos> positions = new ArrayList<BlockPos>();
	protected List<IBlockState> blocks = new ArrayList<IBlockState>();

	public void addRequirement(BlockPos offset, IBlockState block, boolean symmetrical) {
		positions.add(offset);
		blocks.add(block);
		if (symmetrical) {
			positions.add(new BlockPos(-offset.getX(), offset.getY(), offset.getZ()));
			positions.add(new BlockPos(offset.getX(), offset.getY(), -offset.getZ()));
			positions.add(new BlockPos(-offset.getX(), offset.getY(), -offset.getZ()));
			blocks.add(block);
			blocks.add(block);
			blocks.add(block);
		}
		if (positions.size() != blocks.size()) {
			Log.warn("BrewingSetup: Positions and Blocks list are desynchronized");
		}
	}

	/**
	 * @return array with offsets [Xmin, Xmax, Ymin, Ymax, Zmin, Zmax]
	 */
	protected int[] getSearchDimensions() {
		int[] data = new int[6];
		for (BlockPos p : positions) {
			int x = p.getX();
			int y = p.getY();
			int z = p.getZ();

			if (x < 0 && x < data[0]) {
				data[0] = x;
			} else if (x > 0 && x > data[1]) {
				data[1] = x;
			}

			if (y < 0 && y < data[2]) {
				data[0] = y;
			} else if (y > 0 && y > data[3]) {
				data[1] = y;
			}

			if (z < 0 && z < data[0]) {
				data[0] = z;
			} else if (z > 0 && z > data[1]) {
				data[1] = z;
			}
		}
		return data;
	}

	public boolean checkSetup(World world, BlockPos posIn, TEPotionBrewer tile) {
		Iterator<BlockPos> iterator;
		if (positions.size() > 0) {
			int[] dims = getSearchDimensions();
			iterator = BlockPos.getAllInBox(posIn.add(dims[0], dims[2], dims[4]), posIn.add(dims[1], dims[3], dims[5])).iterator();
			while (iterator.hasNext()) {
				BlockPos pos = iterator.next();
				BlockPos relativePos = pos.add(-posIn.getX(), -posIn.getY(), -posIn.getZ());
				if (positions.contains(relativePos)) {
					if (world.getBlockState(pos) != blocks.get(positions.indexOf(relativePos))) {
						return false;
					}
				} else if (relativePos.getY() >= 0 && world.getBlockState(pos).getBlock() != Blocks.air) {
					return false;
				}
			}
		} else {
			iterator = BlockPos.getAllInBox(posIn, posIn.offset(EnumFacing.SOUTH, searchRange).offset(EnumFacing.EAST, searchRange)).iterator();
			List<BlockPos> interestingBlocks = new ArrayList<BlockPos>();
			while (iterator.hasNext()) {
				BlockPos p = iterator.next();
				if (tile.getDistanceSq(p.getX(), p.getY(), p.getZ()) <= searchRange * searchRange && world.getBlockState(p).getBlock() instanceof IBrewingMultiblock) {
					interestingBlocks.add(p);
				}
			}
			for (BlockPos p : interestingBlocks) {
				int dx = p.getX() - posIn.getX();
				int dz = p.getZ() - posIn.getZ();
				if (world.getBlockState(posIn.offset(EnumFacing.EAST, dx).offset(EnumFacing.SOUTH, dz)) != world.getBlockState(posIn.offset(EnumFacing.WEST, dx).offset(EnumFacing.SOUTH, dz))) {
					return false;
				}
				if (world.getBlockState(posIn.offset(EnumFacing.WEST, dx).offset(EnumFacing.SOUTH, dz)) != world.getBlockState(posIn.offset(EnumFacing.WEST, dx).offset(EnumFacing.NORTH, dz))) {
					return false;
				}
				if (world.getBlockState(posIn.offset(EnumFacing.WEST, dx).offset(EnumFacing.NORTH, dz)) != world.getBlockState(posIn.offset(EnumFacing.EAST, dx).offset(EnumFacing.NORTH, dz))) {
					return false;
				}
				if (world.getBlockState(posIn.offset(EnumFacing.EAST, dx).offset(EnumFacing.NORTH, dz)) != world.getBlockState(posIn.offset(EnumFacing.EAST, dx).offset(EnumFacing.SOUTH, dz))) {
					return false;
				}
			}
		}
		return true;
	}

	public List<TEPedestal> getPedestals(World world, BlockPos posIn, TileEntity cauldron) {
		List<TEPedestal> list = new ArrayList<TEPedestal>();
		if (positions.size() > 0) {
			for (BlockPos p : positions) {
				TileEntity tile = world.getTileEntity(posIn.add(p.getX(), p.getY(), p.getZ()));
				if (tile instanceof TEPedestal) {
					list.add((TEPedestal) tile);
				}
			}
		} else {
			Iterator<BlockPos> iterator = BlockPos.getAllInBox(posIn, posIn.add(searchRange, 0, searchRange)).iterator();
			List<BlockPos> interestingBlocks = new ArrayList<BlockPos>();
			while (iterator.hasNext()) {
				BlockPos p = iterator.next();
				if (cauldron.getDistanceSq(p.getX(), p.getY(), p.getZ()) <= searchRange * searchRange) {
					TileEntity tile = world.getTileEntity(p);
					if (tile != null && tile instanceof TEPedestal) {
						interestingBlocks.add(p);
						list.add((TEPedestal) tile);
					}
				}
			}
			for (BlockPos p : interestingBlocks) {
				BlockPos pos = posIn.add(p.getX() - posIn.getX(), 0, posIn.getZ() - p.getZ());
				list.add((TEPedestal) world.getTileEntity(pos));
				pos = posIn.add(posIn.getX() - p.getX(), 0, p.getZ() - posIn.getZ());
				list.add((TEPedestal) world.getTileEntity(pos));
				pos = posIn.add(posIn.getX() - p.getX(), 0, posIn.getZ() - p.getZ());
				list.add((TEPedestal) world.getTileEntity(pos));
			}
		}
		return list;
	}

	public List<TEObelisk> getObelisks(World world, BlockPos posIn, TileEntity cauldron) {
		List<TEObelisk> list = new ArrayList<TEObelisk>();
		if (positions.size() > 0) {
			for (BlockPos p : positions) {
				TileEntity tile = world.getTileEntity(posIn.add(p.getX(), p.getY(), p.getZ()));
				if (tile instanceof TEObelisk) {
					list.add((TEObelisk) tile);
				}
			}
		} else {
			Iterator<BlockPos> iterator = BlockPos.getAllInBox(posIn, posIn.add(searchRange, 0, searchRange)).iterator();
			List<BlockPos> interestingBlocks = new ArrayList<BlockPos>();
			while (iterator.hasNext()) {
				BlockPos p = iterator.next();
				if (cauldron.getDistanceSq(p.getX(), p.getY(), p.getZ()) <= searchRange * searchRange) {
					TileEntity tile = world.getTileEntity(p);
					if (tile != null && tile instanceof TEObelisk) {
						interestingBlocks.add(p);
						list.add((TEObelisk) tile);
					}
				}
			}
			for (BlockPos p : interestingBlocks) {
				BlockPos pos = posIn.add(p.getX() - posIn.getX(), 0, posIn.getZ() - p.getZ());
				list.add((TEObelisk) world.getTileEntity(pos));
				pos = posIn.add(posIn.getX() - p.getX(), 0, p.getZ() - posIn.getZ());
				list.add((TEObelisk) world.getTileEntity(pos));
				pos = posIn.add(posIn.getX() - p.getX(), 0, posIn.getZ() - p.getZ());
				list.add((TEObelisk) world.getTileEntity(pos));
			}
		}
		return list;
	}

	public List<TileEntity> getOtherThing(World world, BlockPos pos, Class<? extends TileEntity> otherThing) {
		List<TileEntity> list = new ArrayList<TileEntity>();
		if (positions.size() > 0) {
			for (BlockPos p : positions) {
				TileEntity tile = world.getTileEntity(pos.add(p.getX(), p.getY(), p.getZ()));
				if (otherThing.isInstance(tile)) {
					list.add(tile);
				}
			}
		}
		return list;
	}

	/**
	 * Ensures symmetry in setup
	 */
	public static BrewingSetup defaultSetup = new BrewingSetup();

}
