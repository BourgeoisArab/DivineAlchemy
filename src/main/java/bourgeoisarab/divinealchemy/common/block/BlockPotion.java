package bourgeoisarab.divinealchemy.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.IBlockEffect;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPotion extends BlockFluidClassic implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	protected static final int duration = 100;

	public BlockPotion() {
		super(ModFluids.potion, Material.water);
		ModFluids.potion.setBlock(this);
		setBlockName("potion");
		setQuantaPerBlock(4);
		isBlockContainer = true;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 0 || side == 1 ? stillIcon : flowingIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon(Ref.Location.PREFIX + "potion_still");
		flowingIcon = register.registerIcon(Ref.Location.PREFIX + "potion_flow");
		ModFluids.potion.setIcons(stillIcon, flowingIcon);
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);

		if (block.getMaterial().isLiquid()) {
			return false;
		}
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof TEPotion) {
			TEPotion tile = (TEPotion) entity;
			if (block == ModBlocks.potion && (tile.getEffects() == null || tile.getEffects().size() < 1)) {
				return true;
			}
		}
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		// if (meta == 0) {
		return new TEPotion();
		// } else {
		// return null;
		// }
	}

	public TEPotion getTileEntity(IBlockAccess world, int x, int y, int z) {
		// if (world.getBlockMetadata(x, y, z) == 0) {
		return (TEPotion) world.getTileEntity(x, y, z);
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
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 100;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		TEPotion tile = getTileEntity(world, x, y, z);

		if (entity instanceof EntityLivingBase && tile != null) {

			List<PotionEffect> effects = tile.getEffects().getEffects();

			if (effects != null) {
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
						((IBlockEffect) p).applyBlockEffect(world, x, y, z, (EntityLivingBase) entity, activeEffect);
					}
				}
			}
		}
	}

	@Override
	protected void flowIntoBlock(World world, int x, int y, int z, int meta) {
		super.flowIntoBlock(world, x, y, z, meta);
		if (world.getTileEntity(x, y, z) instanceof TEPotion) {
			TEPotion t = (TEPotion) world.getTileEntity(x, y, z);
			if (t.getEffects() != null && t.getEffects().size() <= 0) {
				Effects effects = null;
				Colouring colouring = null;
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					TileEntity entity = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if (entity instanceof TEPotion) {
						TEPotion tile = (TEPotion) entity;
						effects = tile.getEffects();
						colouring = tile.getColouring();
						break;
					}
				}
				t.setEffects(effects);
				t.setColouring(colouring);
			}
		}
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		// Overrides super method to fix refilling of potion buckets when CoFHCore is installed
		// Uses custom bucket handler {@link BucketHandler}
		return false;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		if (!isSourceBlock(world, x, y, z)) {
			return null;
		}
		if (doDrain) {
			world.setBlock(x, y, z, Blocks.air);
		}

		if (world.getTileEntity(x, y, z) instanceof TEPotion) {
			TEPotion tile = (TEPotion) world.getTileEntity(x, y, z);
			return NBTEffectHelper.setEffectsForFluid(stack.copy(), tile.getEffects());
		}
		return stack.copy();
	}

	@Override
	public void breakBlock(World world, int xCoord, int yCoord, int zCoord, Block block, int i1) {
		Effects effects = ((TEPotion) world.getTileEntity(xCoord, yCoord, zCoord)).getEffects();
		super.breakBlock(world, xCoord, yCoord, zCoord, block, i1);
		world.removeTileEntity(xCoord, yCoord, zCoord);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity entity = world.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (entity instanceof TEPotion && !isSourceBlock(world, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)) {
				if (effects == ((TEPotion) entity).getEffects()) {
					world.setBlockToAir(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				}
			}
		}
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int n, int m) {
		super.onBlockEventReceived(world, x, y, z, n, m);
		TileEntity tile = world.getTileEntity(x, y, z);
		return tile != null ? tile.receiveClientEvent(n, m) : false;
	}

}
