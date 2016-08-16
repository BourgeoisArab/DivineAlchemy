package bourgeoisarab.divinealchemy.common.potion.ingredient;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionBrewer;

public class IngredientGunpowder extends PotionIngredient {

	public IngredientGunpowder(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(TEPotionBrewer tile) {
		tile.getProperties().isSplash = true;
	}

}
