package bourgeoisarab.divinealchemy.common.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;

public class BrewingSetup {

	public static final int searchRange = 7; // 8 including cauldron block

	public boolean checkSetup(World world, BlockPos posIn, TEBrewingCauldron tile) {
		Iterator<BlockPos> iterator = BlockPos.getAllInBox(posIn, posIn.offset(EnumFacing.SOUTH, searchRange).offset(EnumFacing.EAST, searchRange)).iterator();
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
		return true;
	}

	/**
	 * Ensures symmetry in setup
	 */
	public static BrewingSetup defaultSetup = new BrewingSetup();

}
