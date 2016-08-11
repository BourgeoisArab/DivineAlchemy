package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class ItemBucketPotion extends ItemBucket implements IFluidContainerItem {

	private int capacity;

	public ItemBucketPotion() {
		super(ModBlocks.potion);
		setUnlocalizedName("bucketPotion");
		setContainerItem(Items.bucket);
		setCapacity(FluidContainerRegistry.BUCKET_VOLUME);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			return 0xFFFFFF;
		}
		Effects effects = NBTEffectHelper.getEffects(stack);
		if (effects == null) {
			return ColourHelper.potionColourInt;
		}
		Colouring colouring = NBTEffectHelper.getColouring(stack);
		int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), colouring));
		return colour == -1 ? ColourHelper.potionColourInt : colour;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		Effects effects = NBTEffectHelper.getEffects(stack);
		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
				String s1 = I18n.format(potion.getName());
				list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1)));
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if (NBTEffectHelper.getEffects(stack) != null) {
			if (NBTEffectHelper.getEffects(stack).size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		FluidStack fluid = getFluid(stack);
		ItemStack stackReturn = super.onItemRightClick(stack, world, player);

		MovingObjectPosition mopos = getMovingObjectPositionFromPlayer(world, player, false);
		if (mopos == null) {
			return stack;
		}

		BlockPos pos = mopos.getBlockPos().offset(mopos.sideHit);

		if (world.getBlockState(pos).getBlock() == ModBlocks.potion) {
			((TEPotion) world.getTileEntity(pos)).setFluid(fluid);
		}
		world.markBlockForUpdate(pos);
		return stackReturn;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean currentItem) {
		FluidStack fluid = getFluid(stack);
		if (fluid == null || fluid.amount <= 0) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.inventory.setInventorySlotContents(slot, new ItemStack(Items.bucket, stack.stackSize));
			}
		}
	}

	@Override
	public FluidStack getFluid(ItemStack stack) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Fluid")) {
			return null;
		}
		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
	}

	public Item setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	@Override
	public int getCapacity(ItemStack stack) {
		return capacity;
	}

	@Override
	public int fill(ItemStack stack, FluidStack fluid, boolean doFill) {
		if (fluid == null || fluid != null && fluid.getFluid() != ModFluids.potion) {
			Log.warn("Tried to fill potion bucket with non-potion fluid");
			return 0;
		}

		if (!doFill) {
			if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Fluid")) {
				return Math.min(capacity, fluid.amount);
			}

			FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));

			if (fluidStack == null) {
				return Math.min(capacity, fluid.amount);
			}

			if (!fluidStack.isFluidEqual(fluid)) {
				return 0;
			}
			return Math.min(capacity - fluidStack.amount, fluid.amount);
		}

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (!stack.getTagCompound().hasKey("Fluid")) {
			NBTTagCompound fluidTag = fluid.writeToNBT(new NBTTagCompound());

			if (capacity < fluid.amount) {
				fluidTag.setInteger("Amount", capacity);
				stack.getTagCompound().setTag("Fluid", fluidTag);
				return capacity;
			}

			stack.getTagCompound().setTag("Fluid", fluidTag);
			return fluid.amount;
		}

		NBTTagCompound fluidTag = stack.getTagCompound().getCompoundTag("Fluid");
		FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTag);

		if (!fluidStack.isFluidEqual(fluid)) {
			return 0;
		}

		int filled = capacity - fluidStack.amount;
		if (fluid.amount < filled) {
			fluidStack.amount += fluid.amount;
			filled = fluid.amount;
		} else {
			fluidStack.amount = capacity;
		}

		stack.getTagCompound().setTag("Fluid", fluidStack.writeToNBT(fluidTag));
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Fluid")) {
			return null;
		}
		FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
		if (fluid == null) {
			return null;
		}

		int currentAmount = fluid.amount;
		fluid.amount = Math.min(fluid.amount, maxDrain);
		if (doDrain) {
			if (currentAmount == fluid.amount) {
				stack.getTagCompound().removeTag("Fluid");

				if (stack.getTagCompound().hasNoTags()) {
					stack.setTagCompound(null);
				}
				return fluid;
			}

			NBTTagCompound fluidTag = stack.getTagCompound().getCompoundTag("Fluid");
			fluidTag.setInteger("Amount", currentAmount - fluid.amount);
			stack.getTagCompound().setTag("Fluid", fluidTag);
		}
		return fluid;
	}

}
