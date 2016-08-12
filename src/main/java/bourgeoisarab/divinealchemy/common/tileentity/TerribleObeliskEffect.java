package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.init.ModBlocks;

public abstract class TerribleObeliskEffect {

	public final int stage;
	public final int energy;

	public TerribleObeliskEffect(int stage, int energy) {
		this.stage = stage;
		this.energy = energy;
	}

	public abstract boolean performTask(TileEntity tile, int range, boolean all);

	public static class BlockDecay extends TerribleObeliskEffect {

		protected final Block[] from;
		protected final Block to;

		public BlockDecay(int stage, int energy, Block to, Block... from) {
			super(stage, energy);
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean performTask(TileEntity tile, int range, boolean all) {
			Iterator<BlockPos> blocks = BlockPos.getAllInBox(tile.getPos().offset(EnumFacing.DOWN, range).offset(EnumFacing.NORTH, range).offset(EnumFacing.EAST, range),
					tile.getPos().offset(EnumFacing.UP, range).offset(EnumFacing.SOUTH, range).offset(EnumFacing.WEST, range)).iterator();
			List<BlockPos> desiredBlocks = new ArrayList<BlockPos>();
			while (blocks.hasNext()) {
				BlockPos pos = blocks.next();
				if (tile.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= range * range) {
					if (ArrayUtils.contains(from, tile.getWorld().getBlockState(pos).getBlock())) {
						desiredBlocks.add(pos);
					}
				}
			}
			if (desiredBlocks.size() > 0) {
				if (all) {
					for (BlockPos pos : desiredBlocks) {
						tile.getWorld().setBlockState(pos, to.getDefaultState(), 3);
					}
					return false;
				}
				if (!tile.getWorld().isRemote) {
					BlockPos pos = desiredBlocks.get(tile.getWorld().rand.nextInt(desiredBlocks.size()));
					tile.getWorld().setBlockState(pos, to.getDefaultState(), 3);
				}
				return true;
			}
			return false;
		}
	}

	public static TerribleObeliskEffect grassDecay = new BlockDecay(1, 50, Blocks.dirt, Blocks.grass, Blocks.farmland);
	public static TerribleObeliskEffect leafDecay = new BlockDecay(1, 50, Blocks.air, Blocks.leaves, Blocks.leaves2, Blocks.tallgrass, Blocks.double_plant, Blocks.red_flower, Blocks.yellow_flower, Blocks.cactus);
	public static TerribleObeliskEffect cropDecay = new BlockDecay(1, 50, Blocks.air, Blocks.carrots, Blocks.potatoes, Blocks.wheat, Blocks.pumpkin, Blocks.pumpkin_stem, Blocks.melon_block, Blocks.melon_stem);
	public static TerribleObeliskEffect dirtDecay = new BlockDecay(2, 50, ModBlocks.deadDirt, Blocks.dirt);
	public static TerribleObeliskEffect woodDecay = new BlockDecay(2, 50, ModBlocks.deadWood, Blocks.log, Blocks.log2);
	public static TerribleObeliskEffect creatureDamage = new TerribleObeliskEffect(3, 50) {

		@Override
		public boolean performTask(TileEntity tile, int range, boolean all) {
			BlockPos pos = tile.getPos();
			List<EntityLivingBase> entities = tile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
					AxisAlignedBB.fromBounds(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range));
			for (EntityLivingBase entity : entities) {
				if (tile.getDistanceSq(entity.posX, entity.posY, entity.posZ) <= range * range) {
					entity.attackEntityFrom(DamageSource.magic, 1);
					if (all) {
						return false;
					}
					return true;
				}
			}
			return false;
		}

	};
	public static TerribleObeliskEffect creatureKill = new TerribleObeliskEffect(4, 100) {

		@Override
		public boolean performTask(TileEntity tile, int range, boolean all) {
			BlockPos pos = tile.getPos();
			List<EntityLivingBase> entities = tile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
					AxisAlignedBB.fromBounds(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range));
			for (EntityLivingBase entity : entities) {
				if (!(entity instanceof EntityPlayer) && tile.getDistanceSq(entity.posX, entity.posY, entity.posZ) <= range * range) {
					entity.attackEntityFrom(DamageSource.magic, entity.getHealth());
					if (all) {
						return false;
					}
					return true;
				}
			}
			return false;
		}

	};

}
