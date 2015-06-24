package bourgeoisarab.divinealchemy.common.potion.ingredient;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;

public class IngredientNetherStar extends PotionIngredient {

	public IngredientNetherStar(ItemStack stack, int instability) {
		super(stack, instability);
	}

	@Override
	public void applyEffect(TEBrewingCauldron entity) {
		entity.properties.isPersistent = true;
	}

}
