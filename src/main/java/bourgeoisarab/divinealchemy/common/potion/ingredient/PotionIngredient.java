package bourgeoisarab.divinealchemy.common.potion.ingredient;

import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionIngredient {

	public static final List<PotionIngredient> ingredients = new ArrayList<PotionIngredient>();

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
	public static final PotionIngredient witherSkull = new PotionIngredient(new ItemStack(Items.skull), 0, Potion.wither).setMetaSensitive(true);
	public static final PotionIngredient feather = new PotionIngredient(new ItemStack(Items.feather), 0, ModPotion.potionFlight);

	public static final PotionIngredient gunpowder = new IngredientGunpowder(new ItemStack(Items.gunpowder), 0);
	public static final PotionIngredient netherStar = new IngredientNetherStar(new ItemStack(Items.nether_star), 0);
	public static final PotionIngredient potato = new IngredientPotato(new ItemStack(Items.potato), 0);

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

	public void applyEffect(TEBrewingCauldron tile) {
		tile.addEffect(getEffect(tile));

		if (tile.unstable) {
			addSideEffect(tile, !effect.isBadEffect());
		}

	}

	public Potion getPotion() {
		return Potion.potionTypes[getEffectID()];
	}

	public ItemStack getItem() {
		return stack;
	}

	public NBTTagCompound getNBT() {
		return stack.stackTagCompound;
	}

	public static void register() {
		registerIngredient(magmaCream);
		registerIngredient(sugar);
		registerIngredient(melon);
		registerIngredient(spiderEye);
		registerIngredient(ghastTear);
		registerIngredient(blazePowder);
		registerIngredient(fermentedSpiderEye);
		registerIngredient(goldenCarrot);
		registerIngredient(fish);
		registerIngredient(rottenFlesh);
		registerIngredient(witherSkull);
		registerIngredient(netherStar);
	}

	public static void addSideEffect(TEBrewingCauldron tile, boolean badEffect) {
		List<PotionEffect> sideEffects = null;

		if (!tile.getWorldObj().isRemote && tile.unstable) {
			sideEffects = ModPotionHelper.getSideEffects(tile, badEffect, tile.getWorldObj().rand);
		}

		if (sideEffects != null) {
			for (int i = 0; i < sideEffects.size(); i++) {
				tile.addSideEffect(sideEffects.get(i));
			}
		}
	}

	public static void registerIngredient(PotionIngredient ingredient) {
		if (!ingredients.contains(ingredient)) ingredients.add(ingredient);
	}

	public static PotionIngredient getIngredient(ItemStack stack) {
		for (int i = 0; i < ingredients.size(); i++) {
			PotionIngredient ingredient = ingredients.get(i);
			if (ingredient.getItem().getItem() == stack.getItem()) {
				if (ingredient.getNBT() == null || ingredient.getNBT().equals(stack.stackTagCompound)) {
					if (ingredient.metaSensitive) {
						if (ingredient.getItem().getItemDamage() == stack.getItemDamage()) {
							return ingredient;
						}
					} else {
						return ingredient;
					}
				}
			}
		}
		return null;
	}

	public PotionEffect getEffect(TEBrewingCauldron tile) {
		int duration = tile.unstable ? new Random().nextInt(tile.getMaxDuration() / 4) + (tile.getMaxDuration() / 4) : tile.getMaxDuration();
		return new PotionEffect(effect.id, duration, getAmplifier(tile.countIngredient(this), getMaxAmplifier()));
	}

	public int getEffectID() {
		return effect.id;
	}

	public int getIngredientCount(PotionIngredient[] ingredients) {
		int count = 0;
		for (PotionIngredient i : ingredients) {
			if (getEffectID() == i.getEffectID()) {
				count++;
			}
		}
		return count;
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

}
