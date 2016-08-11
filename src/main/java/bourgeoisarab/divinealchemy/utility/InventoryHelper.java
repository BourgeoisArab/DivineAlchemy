package bourgeoisarab.divinealchemy.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;

public class InventoryHelper {

	public static void addStackToInventory(EntityPlayer player, ItemStack oldStack, ItemStack newStack, boolean replace) {
		if (player.capabilities.isCreativeMode || newStack == null) {
			return;
		}
		if (oldStack.stackSize <= 1 && replace) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
		} else {
			oldStack.stackSize--;
			if (!player.inventory.addItemStackToInventory(newStack)) {
				player.dropPlayerItemWithRandomChoice(newStack, false);
			}
		}
	}

	public static ItemStack processContainer(ItemStack stack, IFluidHandler tile, EnumFacing dir) {
		if (stack == null) {
			return null;
		}
		Item i = stack.getItem();
		FluidStack toDrain = tile.drain(dir, FluidContainerRegistry.BUCKET_VOLUME, false);
		if (i instanceof IFluidContainerItem) {
			IFluidContainerItem fluidContainer = (IFluidContainerItem) stack.getItem();
			FluidStack storedFluid = fluidContainer.getFluid(stack);
			if (storedFluid == null || storedFluid.amount <= 0) {
				fluidContainer.fill(stack, tile.drain(dir, fluidContainer.getCapacity(stack), true), true);
			} else if (tile.canFill(dir, storedFluid.getFluid())) {
				tile.fill(dir, fluidContainer.drain(stack, fluidContainer.getCapacity(stack), true), true);
				storedFluid = fluidContainer.getFluid(stack);
				if (storedFluid == null || storedFluid.amount <= 0) {
					if (i == ModItems.bottlePotion) {
						return new ItemStack(Items.glass_bottle);
					} else if (i == ModItems.bucketPotion) {
						return new ItemStack(Items.bucket);
					}
				}
			}
		} else if (toDrain != null && toDrain.getFluid() == ModFluids.potion && toDrain.amount >= ModItems.bottlePotion.getCapacity(new ItemStack(ModItems.bottlePotion))) {
			ItemStack returnStack = stack;
			if (i == Items.bucket && toDrain.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
				returnStack = new ItemStack(ModItems.bucketPotion);
				ModItems.bucketPotion.fill(returnStack, tile.drain(dir, ((IFluidContainerItem) returnStack.getItem()).getCapacity(returnStack), true), true);
			} else if (i == Items.glass_bottle) {
				// int uses = 1;
				// returnStack = new ItemStack(ModItems.bottlePotion, 1, uses);
				returnStack = new ItemStack(ModItems.bottlePotion);
				ModItems.bottlePotion.fill(returnStack, tile.drain(dir, ((IFluidContainerItem) returnStack.getItem()).getCapacity(returnStack), true), true);
			}
			return returnStack;
		} else if (FluidContainerRegistry.isContainer(stack)) {
			boolean filled = FluidContainerRegistry.isFilledContainer(stack);
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
			if (filled && tile.fill(dir, fluid, false) > 0) {
				tile.fill(dir, fluid, true);
				return FluidContainerRegistry.drainFluidContainer(stack);
			} else if (!filled) {
				int capacity = FluidContainerRegistry.getContainerCapacity(FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE), stack));
				if (!filled && tile.drain(dir, capacity, false) != null && tile.drain(dir, capacity, false).amount >= capacity) {
					return FluidContainerRegistry.fillFluidContainer(tile.drain(dir, capacity, true), stack);
				}
			}
		}
		return stack;
	}

	public static ItemStack processContainer(ItemStack stack, IFluidHandler tile) {
		return processContainer(stack, tile, EnumFacing.UP);
	}

}
