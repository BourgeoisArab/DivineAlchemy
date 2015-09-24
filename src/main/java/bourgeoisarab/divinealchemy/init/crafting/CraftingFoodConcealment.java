package bourgeoisarab.divinealchemy.init.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.item.ItemOrgan;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class CraftingFoodConcealment implements IRecipe {

	private ItemStack result = null;

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		int stackCount = 0;
		ItemStack food = null;
		ItemStack concealer = null;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack j = inv.getStackInSlot(i);
			if (j != null) {
				if (stackCount++ > 2) {
					return false;
				}
				if (j.getItem() instanceof ItemFood) {
					food = j;
				} else if (j.getItem() instanceof ItemOrgan) {
					concealer = j;
				}
			}
		}
		if (stackCount == 2 && food != null && NBTEffectHelper.getEffectsFromStack(food) != null && concealer != null) {
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack j = inv.getStackInSlot(i);
			if (j != null) {
				if (j.getItem() instanceof ItemFood && j.stackTagCompound != null) {
					ItemStack returnStack = j.copy();
					returnStack.stackTagCompound.setBoolean(NBTNames.HIDDEN_EFFECTS, !returnStack.stackTagCompound.getBoolean(NBTNames.HIDDEN_EFFECTS));
					return returnStack;
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
