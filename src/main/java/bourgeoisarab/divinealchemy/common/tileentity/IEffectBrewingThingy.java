package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;

public interface IEffectBrewingThingy extends IEffectProvider {

	public boolean addIngredient(ItemStack ingredient);

	public PotionIngredient[] getIngredients();

	public void addEffect(PotionEffect effect);

	public void addSideEffect(PotionEffect effect);

	public int getMaxDuration();

	public void clearInstability();

	public void finaliseEffects();

	public int getTier();

}
