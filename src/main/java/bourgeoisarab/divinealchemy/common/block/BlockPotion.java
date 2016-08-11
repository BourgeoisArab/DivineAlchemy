package bourgeoisarab.divinealchemy.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.IBlockEffect;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class BlockPotion extends BlockFluidClassic implements ITileEntityProvider {

	protected static final int duration = 100;

	public BlockPotion() {
		super(ModFluids.potion, Material.water);
		ModFluids.potion.setBlock(this);
		setUnlocalizedName("potion");
		setQuantaPerBlock(4);
		isBlockContainer = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int pass) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEPotion) {
			TEPotion tile = (TEPotion) te;
			Effects effects = tile.getEffects();
			return ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), tile.getColouring()));
		}
		return 0xFFFFFF;
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();

		if (block.getMaterial().isLiquid()) {
			return false;
		}
		TileEntity entity = world.getTileEntity(pos);
		if (entity instanceof TEPotion) {
			TEPotion tile = (TEPotion) entity;
			if (block == ModBlocks.potion && (tile.getEffects() == null || tile.getEffects().size() < 1)) {
				return true;
			}
		}
		return super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEPotion();
	}

	public TEPotion getTileEntity(IBlockAccess world, BlockPos pos) {
		// if (world.getBlockMetadata(x, y, z) == 0) {
		return (TEPotion) world.getTileEntity(pos);
		// }
		// int[] coords = getSourceBlockCoords(world, x, y, z);
		// if (coords == null) {
		// return null;
		// }
		// return (TEPotion) world.getTileEntity(coords[0], coords[1], coords[2]);
	}

	// public int[] getSourceBlockCoords(IBlockAccess world, int xCoord, int yCoord, int zCoord) {
	// int x = xCoord;
	// int y = yCoord;
	// int z = zCoord;
	// int[] coords = new int[3];
	//
	// int meta = world.getBlockMetadata(x, y, z);
	// int count = 0;
	// do {
	// int[] blockDir = getSourceBlockDirection(world, x, y, z, meta);
	// if (blockDir == null) {
	// return null;
	// }
	// x = blockDir[0];
	// y = blockDir[1];
	// z = blockDir[2];
	// meta = world.getBlockMetadata(x, y, z);
	// count++;
	// } while (meta != 0 && count < 128);
	// coords[0] = x;
	// coords[1] = y;
	// coords[2] = z;
	// return coords;
	// // TODO: improve this
	// }

	// public int[] getSourceBlockDirection(IBlockAccess world, int x, int y, int z, int meta) {
	// if (world.getBlock(x, y + 1, z) == ModBlocks.blockPotion) {
	// return new int[]{x, y + 1, z};
	// } else if (world.getBlock(x + 1, y, z) == ModBlocks.blockPotion && world.getBlockMetadata(x + 1, y, z) <= meta) {
	// return new int[]{x + 1, y, z};
	// } else if (world.getBlock(x - 1, y, z) == ModBlocks.blockPotion && world.getBlockMetadata(x - 1, y, z) <= meta) {
	// return new int[]{x - 1, y, z};
	// } else if (world.getBlock(x, y, z + 1) == ModBlocks.blockPotion && world.getBlockMetadata(x, y, z + 1) <= meta) {
	// return new int[]{x, y, z + 1};
	// } else if (world.getBlock(x, y, z - 1) == ModBlocks.blockPotion && world.getBlockMetadata(x, y, z - 1) <= meta) {
	// return new int[]{x, y, z - 1};
	// }
	// return null;
	// }

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		TEPotion tile = getTileEntity(world, pos);

		if (entity instanceof EntityLivingBase && tile != null) {

			Effects e = tile.getEffects();
			if (e != null) {
				List<PotionEffect> effects = tile.getEffects().getEffects();
				for (int i = 0; i < effects.size(); i++) {
					PotionEffect effect = new PotionEffect(effects.get(i).getPotionID(), duration, effects.get(i).getAmplifier());

					PotionEffect activeEffect = ((EntityLivingBase) entity).getActivePotionEffect(Potion.potionTypes[effects.get(i).getPotionID()]);
					Potion p = ModPotion.getPotion(effect.getPotionID());
					if (!p.isInstant()) {
						if (activeEffect == null) {
							((EntityLivingBase) entity).addPotionEffect(effect);
						} else if (activeEffect.getDuration() <= duration - 50) {
							((EntityLivingBase) entity).addPotionEffect(effect);
						}
					}
					if (p instanceof IBlockEffect) {
						((IBlockEffect) p).applyBlockEffect(world, pos, (EntityLivingBase) entity, activeEffect);
					}
				}
			}
		}
	}

	@Override
	protected void flowIntoBlock(World world, BlockPos pos, int meta) {
		super.flowIntoBlock(world, pos, meta);
		if (world.getTileEntity(pos) instanceof TEPotion) {
			TEPotion t = (TEPotion) world.getTileEntity(pos);
			Effects e = t.getEffects();
			// Log.info(e);
			if (e == null || e != null && e.size() <= 0) {
				FluidStack fluid = null;
				for (EnumFacing dir : EnumFacing.VALUES) {
					TileEntity entity = world.getTileEntity(pos.offset(dir));
					if (entity instanceof TEPotion) {
						fluid = ((TEPotion) entity).getFluid();
						break;
					}
				}
				t.setFluid(fluid);
			}
		}
	}

	@Override
	public boolean canDrain(World world, BlockPos pos) {
		// Overrides super method to fix refilling of potion buckets when CoFHCore is installed
		// Uses custom bucket handler {@link BucketHandler}
		return super.canDrain(world, pos);
	}

	@Override
	public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
		if (!isSourceBlock(world, pos)) {
			return null;
		}
		if (doDrain) {
			world.setBlockToAir(pos);
		}

		if (world.getTileEntity(pos) instanceof TEPotion) {
			TEPotion tile = (TEPotion) world.getTileEntity(pos);
			return NBTEffectHelper.setEffects(stack.copy(), tile.getEffects());
		}
		return stack.copy();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		FluidStack fluid = ((TEPotion) world.getTileEntity(pos)).getFluid();
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
		for (EnumFacing dir : EnumFacing.VALUES) {
			TileEntity entity = world.getTileEntity(pos.offset(dir));
			if (entity instanceof TEPotion && !isSourceBlock(world, pos.offset(dir))) {
				if (fluid.isFluidEqual(((TEPotion) entity).getFluid())) {
					world.setBlockToAir(pos.offset(dir));
				}
			}
		}
	}

	@Override
	public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int eventID, int eventParam) {
		super.onBlockEventReceived(world, pos, state, eventID, eventParam);
		TileEntity tile = world.getTileEntity(pos);
		return tile != null ? tile.receiveClientEvent(eventID, eventParam) : false;
	}

}
