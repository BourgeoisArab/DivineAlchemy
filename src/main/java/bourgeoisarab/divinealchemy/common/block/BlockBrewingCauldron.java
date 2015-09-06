package bourgeoisarab.divinealchemy.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.InventoryHelper;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;

public class BlockBrewingCauldron extends BlockContainer {

	public List<Block> boilblocks = new ArrayList<Block>();

	public BlockBrewingCauldron() {
		super(Material.iron);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setBlockName("blockBrewingCauldron");
		setBlockTextureName("minecraft:" + "cauldron_side");
		setHardness(1.0F);
		setResistance(2.0F);
		boilblocks.add(Blocks.lava);
		boilblocks.add(Blocks.fire);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEBrewingCauldron();
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for (int i = 0; i < 5; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		setBlockBoundsForItemRender();
	}

	public boolean checkBoil(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		boolean boil = false;
		if (!(world.getTileEntity(x, y, z) instanceof TEBrewingCauldron)) {
			return false;
		}
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		if (boilblocks.contains(block) && tile.tank.getFluidAmount() > 0) {
			boil = true;
		}
		((TEBrewingCauldron) world.getTileEntity(x, y, z)).setBoil(boil);
		return boil;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		checkBoil(world, x, y, z);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
		checkBoil(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		checkBoil(world, x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dunno, float what, float these, float are) {
		checkBoil(world, x, y, z);
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (tile == null || player.isSneaking() || heldStack == null) {
			Log.info("--------------CAULDRON INFO----------------------");
			Log.info("Tier: " + tile.getTier());
			Log.info("Boiling: " + tile.isBoiling());
			Log.info("Cauldron instability:" + tile.getCauldronInstability());
			Log.info("Total instability: " + tile.getInstability());
			Log.info("Ingredients: " + tile.getIngredients());
			Log.info("Effects: " + tile.getEffects());
			Log.info("-------------------------------------------------");
			return false;
		}

		Item heldItem = heldStack.getItem();
		ItemStack returnStack = InventoryHelper.processContainer(heldStack, tile);

		if (tile.tank.getFluidAmount() == 999 && tile.tank.getFluid().getFluid() == FluidRegistry.WATER) {
			tile.tank.fill(new FluidStack(FluidRegistry.WATER, 1), true);
		}

		if (returnStack != null && returnStack != heldStack) {
			if (returnStack.getItem() == ModItems.itemBucketPotion || returnStack.getItem() == ModItems.itemPotionBottle) {
				NBTTagCompound stackTag = returnStack.stackTagCompound;
				returnStack = new ItemStack(returnStack.getItem(), returnStack.stackSize, tile.getProperties().getMetaValue());
				returnStack.stackTagCompound = stackTag;
				NBTEffectHelper.setEffectsForStack(returnStack, tile.getEffects());
				NBTEffectHelper.setColouringForStack(returnStack, tile.getColouring());
			}
			InventoryHelper.addStackToInventory(player, heldStack, returnStack, true);
			if (tile.tank.getFluidAmount() == 1) {
				tile.tank.drain(1, true);
			}
			return true;

		}
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		checkBoil(world, x, y, z);
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		if (tile.isBoiling()) {
			float level = tile.tank.getFluidAmount();
			world.spawnParticle("splash", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			world.spawnParticle("smoke", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			net.minecraft.client.particle.EntityBubbleFX bubble = new net.minecraft.client.particle.EntityBubbleFX(world, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z
					+ (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			DivineAlchemy.proxy.getClient().effectRenderer.addEffect(bubble);
			net.minecraft.client.particle.EntitySplashFX splash = new net.minecraft.client.particle.EntitySplashFX(world, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z
					+ (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			DivineAlchemy.proxy.getClient().effectRenderer.addEffect(splash);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		checkBoil(world, x, y, z);
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		if (tile.tank.getFluid() != null) {
			entity.extinguish();
		}

		if (!tile.isBoiling()) {
			return;
		}

		if (entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			EntityPlayer thrower = world.getPlayerEntityByName(item.getEntityData().getString(NBTNames.EFFECT));
			ItemStack stack = item.getEntityItem();

			net.minecraft.client.particle.EntitySpellParticleFX particle = new net.minecraft.client.particle.EntitySpellParticleFX(world, entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F, 0.0F);
			PotionIngredient ing = PotionIngredient.getIngredient(stack);
			if (!tile.addDye(stack, true) && ing != null) {
				if (!world.isRemote) {
					while (stack.stackSize > 0) {
						if (ing == null) {
							if (world.rand.nextFloat() < 0.1F) {
								tile.makeHotMess();
								break;
							}
						} else if (tile.addIngredient(ing, false)) {
							if (tile.tank.getFluid().getFluid() == FluidRegistry.WATER) {
								tile.tank.setFluid(new FluidStack(ModFluids.fluidPotion, tile.tank.getFluidAmount()));
							}
							if (!PotionIngredient.addSideEffect(tile, !ing.getPotion().isBadEffect(), world.rand)) {
								tile.makeHotMess();
							}
						} else {
							tile.makeHotMess();
						}
						if (thrower != null && ing != null && ing.getPotion() != null) {
							NBTPlayerHelper.addDivinity(thrower, ing.getPotion().isBadEffect() ? -0.0002F : 0.0002F);
						}
						stack.stackSize -= ing.getItem().stackSize;
					}
				}
			}

			if (tile.addDye(stack, false)) {
				float[] colour = ColourHelper.getFloatColours(ColourHelper.separateColours(Colouring.getDyeColour(stack)));
				particle.setRBGColorF(colour[0], colour[1], colour[2]);
			} else if (ing != null) {
				if (ing.getPotion() != null) {
					float[] colour = ColourHelper.getFloatColours(ColourHelper.separateColours(ing.getColour()));
					particle.setRBGColorF(colour[0], colour[1], colour[2]);
				} else {
					particle.setRBGColorF(1.0F, 1.0F, 1.0F);
				}
			} else {
				particle.setRBGColorF(0.0F, 0.0F, 0.0F);
			}
			if (world.isRemote) {
				DivineAlchemy.proxy.getClient().effectRenderer.addEffect(particle);
			}
			entity.setDead();

		} else if (entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).attackEntityFrom(DamageSource.magic, 2.0F);
			if (tile.tank.getFluid().getFluid() == ModFluids.fluidHotMess) {
				ModBlocks.blockHotMess.onEntityCollidedWithBlock(world, x, y, z, entity);
			}
		}
	}
}
