package bourgeoisarab.divinealchemy.common.potion.ingredient;

import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import net.minecraft.item.ItemStack;

public class IngredientGunpowder extends PotionIngredient {

	public IngredientGunpowder(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(TEBrewingCauldron tile) {
		tile.setSplash(true);
	}

}
