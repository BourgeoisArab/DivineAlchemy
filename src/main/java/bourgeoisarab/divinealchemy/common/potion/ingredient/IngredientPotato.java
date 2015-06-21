package bourgeoisarab.divinealchemy.common.potion.ingredient;

import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import net.minecraft.item.ItemStack;

public class IngredientPotato extends PotionIngredient {

	public IngredientPotato(ItemStack stack, int instability) {
		super(stack, instability);
	}

	@Override
	public void applyEffect(TEBrewingCauldron entity) {
		entity.clearInstability();
	}

}
