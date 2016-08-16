package bourgeoisarab.divinealchemy.common.potion.ingredient;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionBrewer;

public class IngredientNetherStar extends PotionIngredient {

	public IngredientNetherStar(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(TEPotionBrewer entity) {
		entity.getProperties().isPersistent = true;
	}

}
