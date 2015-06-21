package bourgeoisarab.divinealchemy.utility;

import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ModPotionHelper {

	/**
	 * List of potion IDs that cannot appear as tier 1 side effects
	 */
	public static final List<Integer> tier1blacklist = new ArrayList<Integer>();

	public static void init() {
		tier1blacklist.add(Potion.blindness.id);
		tier1blacklist.add(Potion.confusion.id);
		tier1blacklist.add(Potion.wither.id);
		tier1blacklist.add(Potion.waterBreathing.id);
		tier1blacklist.add(Potion.invisibility.id);
		tier1blacklist.add(Potion.nightVision.id);
		tier1blacklist.add(ModPotion.potionFlight.id);

		PotionIngredient.register();
		// TODO: Prerequisites to effects and base effects (awkward, mundane, etc.)
	}

	public static Potion getRandomPotion(Random random, boolean badEffect, int tier) {
		Potion potion;
		do {
			potion = Potion.potionTypes[random.nextInt(Potion.potionTypes.length - 1)];
		} while (potion == null || potion.isBadEffect() != badEffect || (tier == 1 && tier1blacklist.contains(potion.id)));
		return potion;
	}

	public static Potion getRandomPotion(Random rand) {
		Potion potion;
		while (true) {
			potion = Potion.potionTypes[rand.nextInt(Potion.potionTypes.length)];
			if (potion != null && !potion.isInstant()) {
				break;
			}
		}
		return potion;
	}

	/**
	 * @param badEffect of the secondary effect; so an opposite random effect will be returned
	 **/
	public static List<PotionEffect> getSideEffects(TEBrewingCauldron tile, boolean badEffect, Random random) {
		float chance = random.nextFloat();
		int duration = tile.unstable ? (int) (tile.getMaxDuration() * tile.getInstability() * (new Random().nextFloat() * 0.5 + 0.5)) : tile.getMaxDuration();
		int tier = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
		int amplifier = tier == 0 ? 0 : tier - 1;

		int number = 0;
		for (int i = 0; i < 3; i++) {
			if (chance < tile.getInstability() / Math.pow(3, i)) {
				number = i;
			} else {
				break;
			}
		}

		List<PotionEffect> effects = new ArrayList<PotionEffect>();

		for (int i = 0; i < number; i++) {
			effects.add(new PotionEffect(getRandomPotion(random, badEffect, tier).id, duration, amplifier));
		}
		return effects;
	}

	// /**
	// * @param badEffect of the primary effect; so an opposite random effect
	// will be returned
	// * **/
	// public static List<PotionEffect> getSideEffects(TEBrewingCauldron tile,
	// boolean badEffect)
	// {
	// float chance = new Random().nextFloat();
	// float instability = tile.getTotalInstability();
	// int duration = tile.unstable ? (int) (tile.getMaxDuration() * instability
	// * (new Random().nextFloat() * 0.5 + 0.5)) : tile.getMaxDuration();
	// int meta = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord,
	// tile.zCoord);
	// int effectTier = meta / 2;
	//
	// if (chance < instability / 9) {
	// return getRandomEffects(3, duration, instability, badEffect ?
	// goodEffectList : badEffectList);
	// }
	// else if (chance < instability / 3) {
	// return getRandomEffects(2, duration, instability, badEffect ?
	// goodEffectList : badEffectList);
	// }
	// else if (chance < instability) {
	// return getRandomEffects(1, duration, instability, badEffect ?
	// goodEffectList : badEffectList);
	// } else {
	// return null;
	// }
	// }
	//
	// private static List<PotionEffect> getRandomEffects(int number, int
	// duration, float instability, ArrayList array)
	// {
	// List<PotionEffect> effectList = new ArrayList<PotionEffect>();
	//
	// for (int i = 0; i < number; i++)
	// {
	// PotionEffect effect;
	// do {
	// int index = new Random().nextInt(array[effectTier].length);
	// effect = new PotionEffect(array[effectTier][index][0], duration,
	// array[effectTier][index][1]);
	// } while (effectList.contains(effect));
	//
	// effectList.add(effect);
	// }
	// return effectList;
	// }

	// public static boolean setEffectsForFluid(FluidStack fluid,
	// List<PotionEffect> effects)
	// {
	// if (!(fluid.getFluid() instanceof FluidPotion)) {
	// FMLLog.warning("",
	// "Tried to set potion NBT data for invalid fluidstack.");
	// return false;
	// }
	// if (fluid.tag == null) fluid.tag = new NBTTagCompound();
	// setEffectsForNBT(fluid.tag, effects);
	// return true;
	// }

	// public static List<PotionEffect> getEffectsFromFluid(FluidStack fluid)
	// {
	// return getEffectsFromNBT(fluid.tag);
	// }

	/**
	 * Converts a list of potion effects to a 2D array. This is used for packets.
	 * 
	 * @return int[ID, duration or amplifier][potion effect index]
	 */
	public static int[][] potionsToIntArray(List<PotionEffect> effects) {
		int[][] array = new int[3][effects.size()];
		for (int i = 0; i < effects.size(); i++) {
			// Arrays are done this way, so a list of IDs is easily obtainable
			array[0][i] = effects.get(i).getPotionID();
			array[1][i] = effects.get(i).getDuration();
			array[2][i] = effects.get(i).getAmplifier();
		}
		return array;
	}

	// /** Converts 2D array of potion effects to a list
	// */
	// public static List<PotionEffect> potionsToList(int[][] array)
	// {
	// List<PotionEffect> effects = new ArrayList<PotionEffect>();
	// for (int i = 0; i < array.length; i++)
	// {
	// effects.add(new PotionEffect(array[0][i], array[1][i], array[2][i]));
	// }
	// return effects;
	// }

	public static void addEffectsToEntity(List<PotionEffect> effects, EntityLivingBase entity) {
		if (effects == null) {
			return;
		}

		for (int i = 0; i < effects.size(); i++) {
			PotionEffect effect = effects.get(i);
			entity.addPotionEffect(new PotionEffect(effect.getPotionID(), effect.getDuration(), effect.getAmplifier()));
		}
	}

	public static int[] mergeIntArrays(int[] a1, int[] a2) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < a1.length; i++) {
			if (!list.contains(a1[i])) list.add(a1[i]);
		}
		for (int i = 0; i < a2.length; i++) {
			if (!list.contains(a2[i])) list.add(a2[i]);
		}
		int[] a3 = new int[list.size()];
		for (int i = 0; i < a3.length; i++) {
			a3[i] = list.get(i);
		}
		return a3;
	}
}
