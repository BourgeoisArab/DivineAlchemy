package bourgeoisarab.divinealchemy.init.crafting;

import bourgeoisarab.divinealchemy.common.item.ItemBottlePotion;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PotionFoodCrafting implements IRecipe {

	private ItemStack result = null;

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		int stackCount = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) stackCount++;
		}
		if (stackCount != 2) {
			return false;
		}

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack1 = inv.getStackInSlot(i);

			if (stack1 != null && stack1.getItem() instanceof ItemFood) {
				if (stack1.stackTagCompound != null && stack1.stackTagCompound.hasKey(Ref.NBT.EFFECTS_TAG)) {
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
					if (potionStack != null && potionStack.getItem() instanceof ItemBottlePotion && potionStack.stackTagCompound != null) {
						ItemStack returnStack;

						// if
						// (!ModPotionHelper.getPotionsFromStack(potionStack).contains(Potion.regeneration.getId()))
						// {
						returnStack = new ItemStack(foodStack.getItem(), 1, foodStack.getItemDamage());

						if (returnStack.stackTagCompound == null) {
							returnStack.stackTagCompound = new NBTTagCompound();
						}
						// }
						// else {
						// returnStack = new ItemStack(ModItems.itemPotionFood);
						//
						// if (returnStack.stackTagCompound == null) {
						// returnStack.stackTagCompound = new NBTTagCompound();
						// }
						//
						// returnStack.setStackDisplayName(foodStack.getDisplayName());
						//
						// ItemFood item = (ItemFood) foodStack.getItem();
						//
						// returnStack.stackTagCompound.setInteger(Reference.NBT.FOOD_ID,
						// Item.getIdFromItem(item));
						// returnStack.stackTagCompound.setInteger(Reference.NBT.FOOD_LEVEL,
						// item.func_150905_g(foodStack));
						// returnStack.stackTagCompound.setFloat(Reference.NBT.FOOD_SATURATION,
						// item.func_150906_h(foodStack));
						// }

						returnStack.stackTagCompound.setTag(Ref.NBT.EFFECTS_TAG, potionStack.stackTagCompound.getCompoundTag(Ref.NBT.EFFECTS_TAG));

						if (potionStack.stackTagCompound.hasKey(Ref.NBT.PERSISTENT)) {
							returnStack.stackTagCompound.setBoolean(Ref.NBT.PERSISTENT, potionStack.stackTagCompound.getBoolean(Ref.NBT.PERSISTENT));
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

}
