package bourgeoisarab.divinealchemy.common.potion.ingredient;

import java.util.Random;

import net.minecraft.item.ItemStack;
import bourgeoisarab.divinealchemy.common.tileentity.IPotionBrewer;

public class IngredientPotato extends PotionIngredient {

	public IngredientPotato(ItemStack stack, int instability) {
		super(stack, instability);
		setDoesEffectStack(false);
	}

	@Override
	public void applyEffect(IPotionBrewer entity, Random rand, boolean sideEffect) {
		entity.clearInstability();
	}

}
