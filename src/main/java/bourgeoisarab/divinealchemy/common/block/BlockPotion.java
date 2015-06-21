package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.Ref;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPotion extends BlockFluidClassic implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	protected static final int duration = 100;

	public BlockPotion() {
		super(ModFluids.fluidPotion, Material.water);
		ModFluids.fluidPotion.setBlock(this);
		setBlockName("blockPotion");
		setQuantaPerBlock(4);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon(Ref.MODID + ":" + "potion_still");
		flowingIcon = register.registerIcon(Ref.MODID + ":" + "potion_flow");
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);

		if (block.getMaterial().isLiquid()) return false;

		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) return false;
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

	public int[] getSourceBlockCoords(IBlockAccess world, int xCoord, int yCoord, int zCoord) {
		int x = xCoord;
		int y = yCoord;
		int z = zCoord;
		int[] coords = new int[3];

		int meta = world.getBlockMetadata(x, y, z);
		int count = 0;
		do {
			int[] blockDir = getSourceBlockDirection(world, x, y, z, meta);
			if (blockDir == null) {
				return null;
			}
			x = blockDir[0];
			y = blockDir[1];
			z = blockDir[2];
			meta = world.getBlockMetadata(x, y, z);
			count++;
		} while (meta != 0 && count < 128);
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
		return coords;
		// TODO: improve this
	}

	public int[] getSourceBlockDirection(IBlockAccess world, int x, int y, int z, int meta) {
		if (world.getBlock(x, y + 1, z) == ModBlocks.blockPotion) {
			return new int[]{x, y + 1, z};
		} else if (world.getBlock(x + 1, y, z) == ModBlocks.blockPotion && world.getBlockMetadata(x + 1, y, z) <= meta) {
			return new int[]{x + 1, y, z};
		} else if (world.getBlock(x - 1, y, z) == ModBlocks.blockPotion && world.getBlockMetadata(x - 1, y, z) <= meta) {
			return new int[]{x - 1, y, z};
		} else if (world.getBlock(x, y, z + 1) == ModBlocks.blockPotion && world.getBlockMetadata(x, y, z + 1) <= meta) {
			return new int[]{x, y, z + 1};
		} else if (world.getBlock(x, y, z - 1) == ModBlocks.blockPotion && world.getBlockMetadata(x, y, z - 1) <= meta) {
			return new int[]{x, y, z - 1};
		}
		return null;
	}

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

			List<PotionEffect> effects = tile.getEffects();

			if (effects != null) {
				for (int i = 0; i < effects.size(); i++) {
					PotionEffect effect = new PotionEffect(effects.get(i).getPotionID(), duration, effects.get(i).getAmplifier());

					PotionEffect activeEffect = ((EntityLivingBase) entity).getActivePotionEffect(Potion.potionTypes[effects.get(i).getPotionID()]);

					if (activeEffect == null) {
						((EntityLivingBase) entity).addPotionEffect(effect);
					} else if (activeEffect.getDuration() <= duration - 50) {
						((EntityLivingBase) entity).addPotionEffect(effect);
					}
				}
			}
		}
	}

	@Override
	protected void flowIntoBlock(World world, int x, int y, int z, int meta) {
		super.flowIntoBlock(world, x, y, z, meta);
		if (world.getTileEntity(x, y, z) instanceof TEPotion) {
			List<PotionEffect> effects = null;
			if (world.getTileEntity(x + 1, y, z) != null && world.getTileEntity(x + 1, y, z) instanceof TEPotion) {
				effects = ((TEPotion) world.getTileEntity(x + 1, y, z)).getEffects();
			} else if (world.getTileEntity(x - 1, y, z) != null && world.getTileEntity(x - 1, y, z) instanceof TEPotion) {
				effects = ((TEPotion) world.getTileEntity(x - 1, y, z)).getEffects();
			} else if (world.getTileEntity(x, y + 1, z) != null && world.getTileEntity(x, y + 1, z) instanceof TEPotion) {
				effects = ((TEPotion) world.getTileEntity(x, y + 1, z)).getEffects();
			} else if (world.getTileEntity(x, y, z + 1) != null && world.getTileEntity(x, y, z + 1) instanceof TEPotion) {
				effects = ((TEPotion) world.getTileEntity(x, y, z + 1)).getEffects();
			} else if (world.getTileEntity(x, y, z - 1) != null && world.getTileEntity(x, y, z - 1) instanceof TEPotion) {
				effects = ((TEPotion) world.getTileEntity(x, y, z - 1)).getEffects();
			}

			TEPotion entity = (TEPotion) world.getTileEntity(x, y, z);

			entity.setEffects(effects);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		// System.out.println(world.getBlockMetadata(x, y, z));
	}

}
