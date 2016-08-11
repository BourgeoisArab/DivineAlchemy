package bourgeoisarab.divinealchemy.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowered;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.InventoryHelper;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;

public class BlockBrewingCauldron extends BlockPowered {

	public static final PropertyInteger PROPERTY_TIER = PropertyInteger.create("tier", 0, 4);

	public BlockBrewingCauldron() {
		super(Material.iron);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setUnlocalizedName("brewingCauldron");
		setHardness(1.0F);
		setResistance(2.0F);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{PROPERTY_TIER});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PROPERTY_TIER, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY_TIER);
	}

	@Override
	public TEPowered createNewTileEntity(World world, int meta) {
		return new TEBrewingCauldron();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
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
		return 3;
	}

	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB bb, List list, Entity entity) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, bb, list, entity);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, bb, list, entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, pos, state, bb, list, entity);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, bb, list, entity);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, bb, list, entity);
		setBlockBoundsForItemRender();
	}

	public boolean checkBoil(IBlockAccess world, BlockPos pos) {
		return ((TEBrewingCauldron) world.getTileEntity(pos)).checkFuelSource();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		checkBoil(world, pos);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
		super.onNeighborBlockChange(world, pos, state, block);
		checkBoil(world, pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float what, float these, float are) {
		checkBoil(world, pos);
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(pos);
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (tile == null || player.isSneaking() || heldStack == null) {
			Log.info("--------------CAULDRON INFO----------------------");
			Log.info("Fluid: " + tile.tank.getFluid());
			Log.info("Tier: " + tile.getTier());
			Log.info("Boiling: " + tile.isBoiling());
			Log.info("Cauldron instability:" + tile.getCauldronInstability());
			// Log.info("Total instability: " + tile.getInstability());
			// Log.info("Ingredients: " + tile.getIngredients());
			// Log.info("Effects: " + tile.getEffects());
			Log.info("-------------------------------------------------");
			return false;
		}

		Item heldItem = heldStack.getItem();
		ItemStack returnStack = InventoryHelper.processContainer(heldStack, tile);

		if (returnStack != null && returnStack != heldStack) {
			// if (returnStack.getItem() == ModItems.bucketPotion || returnStack.getItem() == ModItems.bottlePotion) {
			// NBTTagCompound stackTag = returnStack.stackTagCompound;
			// returnStack = new ItemStack(returnStack.getItem(), returnStack.stackSize, tile.getProperties().getMetaValue());
			// returnStack.stackTagCompound = stackTag;
			// NBTEffectHelper.setEffectsForStack(returnStack, tile.getEffects());
			// NBTEffectHelper.setColouringForStack(returnStack, tile.getColouring());
			// }
			InventoryHelper.addStackToInventory(player, heldStack, returnStack, true);
			if (tile.tank.getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME == 1) {
				tile.tank.getFluid().amount--;
			} else if (tile.tank.getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME == 999) {
				tile.tank.getFluid().amount++;
			}
			return true;

		}
		return false;
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		// checkBoil(world, x, y, z);
		// TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		// if (tile.isBoiling() || tile.isRunning()) {
		// float level = tile.tank.getFluidAmount();
		// world.spawnParticle("splash", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
		// world.spawnParticle("smoke", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
		// net.minecraft.client.particle.EntityBubbleFX bubble = new net.minecraft.client.particle.EntityBubbleFX(world, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z
		// + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
		// DivineAlchemy.proxy.getClient().effectRenderer.addEffect(bubble);
		// net.minecraft.client.particle.EntitySplashFX splash = new net.minecraft.client.particle.EntitySplashFX(world, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tile.tank.getCapacity()), z
		// + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
		// DivineAlchemy.proxy.getClient().effectRenderer.addEffect(splash);
		// }
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		checkBoil(world, pos);
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(pos);
		FluidStack fluid = tile.tank.getFluid();
		if (fluid != null && fluid.getFluid() != ModFluids.potion) {
			fluid.getFluid().getBlock().onEntityCollidedWithBlock(world, pos, entity);
		}
		if (!tile.isBoiling()) {
			return;
		}

		if (entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			EntityPlayer thrower = world.getPlayerEntityByName(item.getEntityData().getString(NBTNames.EFFECT));
			ItemStack stack = item.getEntityItem();

			PotionIngredient ing = PotionIngredient.getIngredient(stack);
			Log.info(ing);
			if (!tile.addDye(stack, true) && ing != null) {
				if (!world.isRemote) {
					while (stack.stackSize > 0) {
						if (ing == null) {
							if (world.rand.nextFloat() < 0.1F) {
								// tile.makeHotMess();
								break;
							}
						} else if (true
						// tile.addIngredient(ing, false)
						) {
							if (tile.tank.getFluid().getFluid() == FluidRegistry.WATER) {
								tile.tank.setFluid(new FluidStack(ModFluids.potion, tile.tank.getFluidAmount()));
							}
							if (!PotionIngredient.addSideEffect(tile, ing, world.rand)) {
								// tile.makeHotMess();
							}
						}
						// else {
						// tile.makeHotMess();
						// }
						if (thrower != null && ing != null && ing.getPotion() != null) {
							NBTPlayerHelper.addDivinity(thrower, ing.getPotion().isBadEffect() ? -0.0002F : 0.0002F);
						}
						stack.stackSize -= ing.getItem().stackSize;
					}
				}
			}

			float[] colour;
			if (tile.addDye(stack, false)) {
				colour = ColourHelper.getFloatColours(ColourHelper.separateColours(Colouring.getDyeColour(stack)));
			} else if (ing != null) {
				if (ing.getPotion() != null) {
					colour = ColourHelper.getFloatColours(ColourHelper.separateColours(ing.getColour()));
				} else {
					colour = new float[]{1.0F, 1.0F, 1.0F};
				}
			} else {
				colour = new float[]{0.0F, 0.0F, 0.0F};
			}
			if (world.isRemote) {
				spawnParticle(world, entity.posX, entity.posY, entity.posZ, colour);
			}
			entity.setDead();

		} else if (entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).attackEntityFrom(DamageSource.magic, 2.0F);
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticle(World world, double x, double y, double z, float[] colour) {
		DivineAlchemy.proxy.getClient().effectRenderer.addEffect(new EntityFX(world, x, y, z, colour[0], colour[1], colour[2]));
	}

}
