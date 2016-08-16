package bourgeoisarab.divinealchemy.common.potion.ingredient;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import bourgeoisarab.divinealchemy.common.block.BrewingSetup;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionBrewer;
import bourgeoisarab.divinealchemy.init.ModItems;

public class PotionIngredient {

	public static final List<PotionIngredient> ingredients = new ArrayList<PotionIngredient>();
	public static final IngredientEssenceCrystal[] crystals = new IngredientEssenceCrystal[256];

	public static final PotionIngredient magmaCream = new PotionIngredient(new ItemStack(Items.magma_cream), 0, Potion.fireResistance);
	public static final PotionIngredient sugar = new PotionIngredient(new ItemStack(Items.sugar), 0, Potion.moveSpeed);
	public static final PotionIngredient melon = new PotionIngredient(new ItemStack(Items.melon), 0, Potion.heal);
	public static final PotionIngredient spiderEye = new PotionIngredient(new ItemStack(Items.spider_eye), 0, Potion.poison);
	public static final PotionIngredient ghastTear = new PotionIngredient(new ItemStack(Items.ghast_tear), 0, Potion.regeneration);
	public static final PotionIngredient blazePowder = new PotionIngredient(new ItemStack(Items.blaze_powder), 0, Potion.damageBoost);
	public static final PotionIngredient fermentedSpiderEye = new PotionIngredient(new ItemStack(Items.fermented_spider_eye), 0, Potion.weakness);
	public static final PotionIngredient goldenCarrot = new PotionIngredient(new ItemStack(Items.golden_carrot), 0, Potion.nightVision);
	public static final PotionIngredient fish = new PotionIngredient(new ItemStack(Items.fish), 0, Potion.waterBreathing);
	public static final PotionIngredient rottenFlesh = new PotionIngredient(new ItemStack(Items.rotten_flesh), 0, Potion.hunger);
	public static final PotionIngredient witherSkull = new PotionIngredient(new ItemStack(Items.skull, 1, 1), 0, Potion.wither).setMetaSensitive(true);
	public static final PotionIngredient feather = new PotionIngredient(new ItemStack(Items.feather), 0, ModPotion.potionFlight);

	public static final PotionIngredient gunpowder = new IngredientGunpowder(new ItemStack(Items.gunpowder), 0);
	public static final PotionIngredient netherStar = new IngredientNetherStar(new ItemStack(Items.nether_star), 0);

	public final int id;
	public final ItemStack stack;
	/**
	 * Additional instability this ingredient may add
	 */
	protected float instability;
	protected Potion effect;
	protected boolean doesEffectStack;
	/**
	 * Whether to check metadata when getting ingredient from an ItemStack
	 */
	protected boolean metaSensitive;
	protected int maxAmplifier;
	protected BrewingSetup brewingSetup;

	public PotionIngredient(ItemStack stack) {
		this(stack, 0);
	}

	public PotionIngredient(ItemStack stack, float instability) {
		this(stack, instability, null, Byte.MAX_VALUE);
	}

	public PotionIngredient(ItemStack stack, float instability, int potionID) {
		this(stack, instability, ModPotion.getPotion(potionID), Byte.MAX_VALUE);
	}

	public PotionIngredient(ItemStack stack, float instability, Potion potion) {
		this(stack, instability, potion, Byte.MAX_VALUE);
	}

	public PotionIngredient(ItemStack stack, float instability, Potion potion, int maxAmplifier) {
		this.stack = stack;
		this.instability = instability;
		effect = potion;
		doesEffectStack = true;
		metaSensitive = false;
		if (potion instanceof ModPotion) {
			brewingSetup = ((ModPotion) potion).brewingSetup;
		} else {
			brewingSetup = BrewingSetup.defaultSetup;
		}
		id = ingredients.size();
		registerIngredient(this);
		if (this instanceof IngredientEssenceCrystal) {
			crystals[potion.id] = (IngredientEssenceCrystal) this;
		}
	}

	public BrewingSetup getBrewingSetup() {
		return brewingSetup;
	}

	public float getInstability() {
		return instability;
	}

	public PotionIngredient setMetaSensitive(boolean metaSensitive) {
		this.metaSensitive = metaSensitive;
		return this;
	}

	public PotionIngredient setDoesEffectStack(boolean doesEffectStack) {
		this.doesEffectStack = doesEffectStack;
		return this;
	}

	/**
	 * Called to apply any additional ingredient properties
	 */
	public void applyEffect(TEPotionBrewer tile) {
	}

	public Potion getPotion() {
		return effect;
	}

	public int getColour() {
		return effect != null ? effect.getLiquidColor() : 0xFFFFFF;
	}

	public ItemStack getItem() {
		return stack;
	}

	public static void registerIngredient(PotionIngredient ingredient) {
		if (!ingredients.contains(ingredient)) {
			ingredients.add(ingredient);
		}
	}

	public static PotionIngredient getIngredient(int id) {
		return id >= 0 && id < ingredients.size() ? ingredients.get(id) : null;
	}

	public static PotionIngredient getIngredient(ItemStack stack) {
		if (stack.getItem() == ModItems.essenceCrystal) {
			return crystals[stack.getItemDamage()];
		}
		for (int i = 0; i < ingredients.size(); i++) {
			PotionIngredient ingredient = ingredients.get(i);
			if (ingredient.getItem().getItem() == stack.getItem() && ingredient.getItem().stackSize <= stack.stackSize) {
				if (ingredient.metaSensitive) {
					if (ingredient.getItem().getItemDamage() == stack.getItemDamage()) {
						return ingredient;
					}
				} else {
					return ingredient;
				}
			}
		}
		return null;
	}

	public int getPotionID() {
		return effect != null ? effect.id : 0;
	}

	public int getMaxAmplifier() {
		// if (this instanceof IngredientEssenceCrystal) {
		// return 3;
		// }
		return maxAmplifier;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + (getItem() != null ? getItem().getItem() : null);
	}

}
