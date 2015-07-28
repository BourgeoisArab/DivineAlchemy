package bourgeoisarab.divinealchemy.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

	public static ItemStack processContainer(ItemStack stack, IFluidHandler tile, ForgeDirection dir) {
		if (FluidContainerRegistry.isContainer(stack)) {
			boolean filled = FluidContainerRegistry.isFilledContainer(stack);
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);
			if (stack.getItem() == Items.potionitem && stack.getItemDamage() == 0) {
				fluid = new FluidStack(FluidRegistry.WATER, 333);
			}
			if (filled && tile.fill(dir, fluid, false) > 0) {
				tile.fill(dir, fluid, true);
				return FluidContainerRegistry.drainFluidContainer(stack);
			} else {
				int capacity = FluidContainerRegistry.getContainerCapacity(FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE), stack));
				if (stack.getItem() == Items.glass_bottle) {
					capacity = 333;
				}
				if (!filled && tile.drain(dir, capacity, false) != null && tile.drain(dir, capacity, false).amount >= capacity) {
					if (stack.getItem() == Items.glass_bottle) {
						Item item = tile.canDrain(dir, ModFluids.fluidPotion) ? ModItems.itemPotionBottle : Items.potionitem;
						tile.drain(dir, 333, true);
						return new ItemStack(item);
					}
					return FluidContainerRegistry.fillFluidContainer(tile.drain(dir, capacity, true), stack);
				}
			}
		}
		return stack;
	}

	public static ItemStack processContainer(ItemStack stack, IFluidHandler tile) {
		return processContainer(stack, tile, ForgeDirection.UP);
	}

}
