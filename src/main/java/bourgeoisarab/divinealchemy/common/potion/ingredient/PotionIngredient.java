package bourgeoisarab.divinealchemy.common.potion.ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.potion.IDivinePotion;
import bourgeoisarab.divinealchemy.common.potion.IEvilPotion;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.IPotionBrewer;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;

public class PotionIngredient {

	public static final List<PotionIngredient> ingredients = new ArrayList<PotionIngredient>();
	public static final IngredientEssenceCrystal[] crystals = new IngredientEssenceCrystal[128];

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
	public static final PotionIngredient potato = new IngredientPotato(new ItemStack(Items.potato), 0);

	public final int id;
	public ItemStack stack;
	/**
	 * Additional instability this ingredient may add
	 */
	public float instability;
	public Potion effect;
	public boolean doesEffectStack;
	/**
	 * Whether to check metadata when getting ingredient from an ItemStack
	 */
	public boolean metaSensitive;
	/**
	 * The ingredient's order of priority when applying effects. Higher values will make it go first and lower values last.
	 */
	private int priority;

	public PotionIngredient(ItemStack stack) {
		this(stack, 0);
	}

	public PotionIngredient(ItemStack stack, float instability) {
		this(stack, instability, null);
	}

	public PotionIngredient(ItemStack stack, float instability, int potionID) {
		this(stack, instability, ModPotion.getPotion(potionID));
	}

	public PotionIngredient(ItemStack stack, float instability, Potion potion) {
		this.stack = stack;
		this.instability = instability;
		effect = potion;
		doesEffectStack = true;
		metaSensitive = false;
		priority = 0;
		id = ingredients.size();
		registerIngredient(this);
		if (this instanceof IngredientEssenceCrystal) {
			crystals[potion.id] = (IngredientEssenceCrystal) this;
		}
	}

	/**
	 * Sets priority for ingredient, which is capped at Integer.MAX_VALUE - 1 aka 2147483646
	 */
	public PotionIngredient setPriority(int priority) {
		this.priority = priority == Integer.MAX_VALUE ? Integer.MAX_VALUE - 1 : priority;
		return this;
	}

	public PotionIngredient setMetaSensitive(boolean metaSensitive) {
		this.metaSensitive = metaSensitive;
		return this;
	}

	public PotionIngredient setDoesEffectStack(boolean doesEffectStack) {
		this.doesEffectStack = doesEffectStack;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public void applyEffect(IPotionBrewer tile, Random rand, boolean sideEffect) {
		tile.addEffect(getEffect(tile, sideEffect), sideEffect);
		tile.removeIngredient(this, ConfigHandler.maxEffects);
	}

	public Potion getPotion() {
		return ModPotion.getPotion(getPotionID());
	}

	public int getColour() {
		return effect != null ? effect.getLiquidColor() : 0xFFFFFF;
	}

	public ItemStack getItem() {
		return stack;
	}

	public NBTTagCompound getNBT() {
		return stack.stackTagCompound;
	}

	/**
	 * @param tile
	 * @param badEffect of the desired side effect
	 * @param rand
	 * @return true, if adding ingredients was successful; false, if no more were accepted
	 */
	public static boolean addSideEffect(IPotionBrewer tile, boolean badEffect, Random rand) {
		if (tile.getProperties().isStable) {
			return true;
		}
		float chance = rand.nextFloat();
		int number = 0;
		for (int i = 0; i < 3; i++) {
			if (chance < tile.getInstability() / Math.pow(3, i)) {
				number++;
			} else {
				break;
			}
		}
		for (int i = 0; i < number; i++) {
			PotionIngredient ing = crystals[rand.nextInt(crystals.length - 1)];
			if (ing == null) {
				continue;
			}
			Potion p = ing.getPotion();
			if (p == null || p.isBadEffect() != badEffect || p instanceof IDivinePotion || p instanceof IEvilPotion || ModPotionHelper.tier1blacklist.contains(p.getId())) {
				continue;
			} else {
				if (!tile.addIngredient(ing, true)) {
					return false;
				}
			}
		}
		return true;
	}

	// /**
	// * @param badEffect of the desired side effect
	// **/
	// public static List<PotionIngredient> getSideEffects(IEffectBrewingThingy tile, boolean badEffect, Random random) {
	// float chance = random.nextFloat();
	// int duration = tile.getProperties().isUnstable ? (int) (tile.getMaxDuration() * tile.getInstability() * (new Random().nextFloat() * 0.5 + 0.5)) : tile.getMaxDuration();
	// int tier = tile.getTier();
	// int amplifier = tier / 2;
	//
	// int number = 0;
	// for (int i = 0; i < 3; i++) {
	// if (chance < tile.getInstability() / Math.pow(3, i)) {
	// number = i;
	// } else {
	// break;
	// }
	// }
	//
	// List<PotionIngredient> ings = new ArrayList<PotionIngredient>();
	//
	// for (int i = 0; i < number; i++) {
	// PotionIngredient ing = ingredients.get(random.nextInt(ingredients.size() - 1));
	// if (ing.getPotion().isBadEffect() != badEffect) {
	// continue;
	// }
	// }
	// return ings;
	// }

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

	public PotionEffect getEffect(IPotionBrewer tile, boolean sideEffect) {
		int duration = !tile.getProperties().isStable ? new Random().nextInt(tile.getMaxDuration() / 4) + tile.getMaxDuration() / 4 : tile.getMaxDuration();
		if (sideEffect) {
			duration = (int) (duration * ((tile.getInstability() + 1) / 2));
		}
		if (getPotion().isInstant()) {
			duration = 1;
		}
		return new PotionEffect(effect.id, duration, getAmplifier(tile.countIngredient(this), getMaxAmplifier()));
	}

	public int getPotionID() {
		return effect != null ? effect.id : 0;
	}

	/**
	 * @param count the number of ingredients present in potion
	 * @param maxLevel the maximum possible amplifier
	 * @return potion amplifier to be used
	 */
	public int getAmplifier(int count, int maxLevel) {
		return Math.min(count - 1, maxLevel);
	}

	public int getMaxAmplifier() {
		if (this instanceof IngredientEssenceCrystal) {
			return 3;
		}
		return 1;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + (getItem() != null ? getItem().getItem() : null);
	}

}
