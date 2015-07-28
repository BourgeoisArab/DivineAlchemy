package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.potion.Ingredients;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;

public interface IEffectBrewingThingy extends IEffectProvider {

	public boolean addIngredient(PotionIngredient ing, boolean side);

	public int removeIngredient(PotionIngredient ing, int amount);

	public Ingredients getIngredients();

	public void setIngredients(Ingredients ingredients);

	public void addEffect(PotionEffect effect, boolean side);

	public int getMaxDuration();

	public void clearInstability();

	/**
	 * Converts all ingredients into PotionEffects and clears all ingredients
	 */
	public void finaliseEffects();

	public void clearEffects();

	/**
	 * @return tier
	 */
	public int getTier();

	/**
	 * @param ing
	 * @return the number of times the specified ingredient occurs
	 */
	public int countIngredient(PotionIngredient ing);

	/**
	 * @param stack
	 * @param add - if false, the adding of the dye will be simulated
	 * @return whether adding of the dye was successful
	 */
	public boolean addDye(ItemStack stack, boolean add);

}
