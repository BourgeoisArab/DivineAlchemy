package bourgeoisarab.divinealchemy.init.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.item.ItemBottlePotion;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class CraftingPotionFood implements IRecipe {

	private ItemStack result = null;

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		int stackCount = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				stackCount++;
			}
		}
		if (stackCount != 2) {
			return false;
		}

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack1 = inv.getStackInSlot(i);

			if (stack1 != null && stack1.getItem() instanceof ItemFood) {
				if (stack1.getTagCompound() != null && stack1.getTagCompound().hasKey(NBTNames.EFFECTS_TAG)) {
					return false;
				}

				for (int j = 0; j < inv.getSizeInventory(); j++) {
					ItemStack stack2 = inv.getStackInSlot(j);
					if (stack2 != null && stack2.getItem() instanceof ItemBottlePotion) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack foodStack = inv.getStackInSlot(i);
			if (foodStack != null && foodStack.getItem() instanceof ItemFood) {
				for (int j = 0; j < inv.getSizeInventory(); j++) {
					ItemStack potionStack = inv.getStackInSlot(j);
					if (potionStack != null && potionStack.getItem() instanceof ItemBottlePotion && potionStack.getTagCompound() != null) {
						ItemStack returnStack;

						returnStack = new ItemStack(foodStack.getItem(), 1, foodStack.getItemDamage());

						if (returnStack.getTagCompound() == null) {
							returnStack.setTagCompound(new NBTTagCompound());
						}

						returnStack.getTagCompound().setTag(NBTNames.EFFECTS_TAG, potionStack.getTagCompound().getCompoundTag(NBTNames.EFFECTS_TAG));

						if (potionStack.getTagCompound().hasKey(NBTNames.PERSISTENT)) {
							returnStack.getTagCompound().setBoolean(NBTNames.PERSISTENT, potionStack.getTagCompound().getBoolean(NBTNames.PERSISTENT));
						}

						result = returnStack.copy();
						return returnStack;
					}
				}
			}
		}
		return null;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		// TODO Auto-generated method stub
		return null;
	}

}
