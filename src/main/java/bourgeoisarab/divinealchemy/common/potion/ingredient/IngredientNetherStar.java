package bourgeoisarab.divinealchemy.common.potion.ingredient;

import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import net.minecraft.item.ItemStack;

public class IngredientNetherStar extends PotionIngredient {

	public IngredientNetherStar(ItemStack stack, int instability) {
		super(stack, instability);
	}

	@Override
	public void applyEffect(TEBrewingCauldron entity) {
		entity.persistent = true;
	}

}
