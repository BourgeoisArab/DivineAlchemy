package bourgeoisarab.divinealchemy.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.NBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBrewingCauldron extends BlockContainer {

	public BlockBrewingCauldron() {
		super(Material.iron);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockName("blockBrewingCauldron");
		setBlockTextureName("minecraft:" + "cauldron_side");
		setHardness(1.0F);
		setResistance(2.0F);
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
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBoundsForItemRender();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		// TEBrewingCauldron entity = (TEBrewingCauldron) world.getTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dunno, float what, float these, float are) {
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (tile == null || player.isSneaking() || heldStack == null) {
			return false;
		}

		Item heldItem = heldStack.getItem();
		ItemStack returnStack = processContainer(heldStack, tile);

		if (returnStack != null) {
			if (returnStack.getItem() == ModItems.itemBucketPotion || returnStack.getItem() == ModItems.itemPotionBottle) {
				NBTHelper.setEffectsForStack(returnStack, tile.getEffects());
			}
			addStackToInventory(player, heldStack, returnStack);
			if (tile.getFluidAmount() == 1) {
				tile.drain(1, true);
			}
			return true;
		}
		return false;
	}

	public void addStackToInventory(EntityPlayer player, ItemStack oldStack, ItemStack newStack) {
		if (player.capabilities.isCreativeMode) {
			return;
		}
		if (oldStack.stackSize <= 1) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
		} else {
			oldStack.stackSize--;
			if (!player.inventory.addItemStackToInventory(newStack)) {
				player.dropPlayerItemWithRandomChoice(newStack, false);
			}
		}
	}

	public ItemStack processContainer(ItemStack stack, IFluidTank tile) {
		if (FluidContainerRegistry.isContainer(stack)) {
			boolean filled = FluidContainerRegistry.isFilledContainer(stack);
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
			if (fluid != null) {
				Log.info(fluid.amount);
			}
			if (filled && tile.fill(fluid, false) > 0) {
				tile.fill(fluid, true);
				return FluidContainerRegistry.drainFluidContainer(stack);
			} else {
				// ItemStack filledStack = FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, 1000), stack);
				// int capacity = FluidContainerRegistry.getContainerCapacity(filledStack);
				int capacity = stack.getItem() == Items.bucket ? FluidContainerRegistry.BUCKET_VOLUME : 333;
				if (!filled && tile.drain(capacity, false) != null && tile.drain(capacity, false).amount > 0) {
					return FluidContainerRegistry.fillFluidContainer(tile.drain(capacity, true), stack);
				}
			}
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		TEBrewingCauldron entity = (TEBrewingCauldron) world.getTileEntity(x, y, z);

		if (entity.isBoiling()) {
			world.spawnParticle("splash", x + (rand.nextFloat() * 0.625 + 0.1875), y + 0.75, z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			world.spawnParticle("smoke", x + (rand.nextFloat() * 0.625 + 0.1875), y + 0.75, z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
		if (tile.getFluid() != null) {
			entity.extinguish();
		}

		if (!tile.isBoiling()) {
			return;
		}

		if (entity instanceof EntityItem) {
			ItemStack stack = ((EntityItem) entity).getEntityItem();

			EntityFX particle = new EntitySpellParticleFX(world, entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F, 0.0F);
			particle = new EntitySpellParticleFX(world, entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F, 0.0F);
			if (tile.addIngredient(stack)) {
				int[] colour = ColourHelper.separateColours(PotionIngredient.getIngredient(stack).getPotion().getLiquidColor());
				// float[] colour = ColourHelper.getFloatColours(ColourHelper.separateColours(PotionIngredient.getIngredient(stack).getPotion().getLiquidColor()));
				for (float i : colour) {
					Log.info(i / 255);
				}
				// particle.setRBGColorF(colour[0] * 255F, colour[1] * 255F, colour[2] * 255F);
				particle.setRBGColorF(colour[0], colour[1], colour[2]);
			} else {
				particle.setRBGColorF(0.0F, 1.0F, 0.0F);
				tile.makeHotMess();
			}
			DivineAlchemy.proxy.getClient().effectRenderer.addEffect(particle);
			entity.setDead();
			// TODO: Instability stuff!!!

		} else if (entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).attackEntityFrom(DamageSource.magic, 2.0F);
			if (tile.getFluid().getFluid() == ModFluids.fluidHotMess) {
				ModBlocks.blockHotMess.onEntityCollidedWithBlock(world, x, y, z, entity);
			}
		}
	}

}
