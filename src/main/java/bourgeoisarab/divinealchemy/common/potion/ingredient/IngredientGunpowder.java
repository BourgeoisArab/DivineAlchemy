package bourgeoisarab.divinealchemy.common.potion.ingredient;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;

public class IngredientGunpowder extends PotionIngredient {

	public IngredientGunpowder(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(TEBrewingCauldron tile) {
		tile.properties.isSplash = true;
	}

}
