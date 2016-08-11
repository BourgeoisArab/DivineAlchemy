package bourgeoisarab.divinealchemy.common.potion.ingredient;

import java.util.Random;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionBrewer;

public class IngredientNetherStar extends PotionIngredient {

	public IngredientNetherStar(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(TEPotionBrewer entity, Random rand, boolean sideEffect) {
		entity.getProperties().isPersistent = true;
	}

}
