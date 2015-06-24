package bourgeoisarab.divinealchemy.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.item.ItemBucketPotion;
import bourgeoisarab.divinealchemy.common.item.ItemPotionBottle;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;

public class NBTHelper {

	public static void initTagCompound(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
	}

	public static NBTTagCompound setIngredientsForNBT(NBTTagCompound tag, PotionIngredient[] ingredients) {
		int[] ids = new int[ingredients.length];
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] != null) {
				ids[i] = ingredients[i].id;
			} else {
				ids[i] = -1;
			}
		}
		tag.setIntArray(Ref.NBT.INGREDIENTS, ids);
		return tag;
	}

	public static PotionIngredient[] getIngredientsFromNBT(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey(Ref.NBT.INGREDIENTS)) {
			return null;
		}
		int[] ids = tag.getIntArray(Ref.NBT.INGREDIENTS);
		PotionIngredient[] ingredients = new PotionIngredient[ConfigHandler.maxEffects];
		for (int i = 0; i < ingredients.length; i++) {
			if (ids[i] >= 0) {
				ingredients[i] = PotionIngredient.ingredients.get(ids[i]);
			}
		}
		return ingredients;
	}

	public static List<PotionEffect> getEffectsFromNBT(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey(Ref.NBT.EFFECTS_TAG)) {
			return null;
		}
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		NBTTagCompound tagEffects = tag.getCompoundTag(Ref.NBT.EFFECTS_TAG);

		for (int i = 0; i < ConfigHandler.maxEffects; i++) {
			if (tagEffects.hasKey(Ref.NBT.EFFECT + i)) {
				int[] e = tagEffects.getIntArray(Ref.NBT.EFFECT + i);
				effects.add(new PotionEffect(e[0], e[1], e[2]));
			} else {
				break;
			}
		}
		return effects;
	}

	public static NBTTagCompound setEffectsForNBT(NBTTagCompound tag, List<PotionEffect> effects) {
		if (effects == null) {
			return null;
		}

		NBTTagCompound tagEffects = new NBTTagCompound();

		for (int i = 0; i < effects.size(); i++) {
			PotionEffect e = effects.get(i);
			tagEffects.setIntArray(Ref.NBT.EFFECT + i, new int[]{e.getPotionID(), e.getDuration(), e.getAmplifier()});
		}
		tag.setTag(Ref.NBT.EFFECTS_TAG, tagEffects);
		return tag;
	}

	public static ItemStack setEffectsForStack(ItemStack stack, List<PotionEffect> effects) {
		if (!(stack.getItem() instanceof ItemBucketPotion || stack.getItem() instanceof ItemPotionBottle)) {
			Log.warn("Tried to set potion NBT data for invalid itemstack.");
		}
		initTagCompound(stack);
		setEffectsForNBT(stack.stackTagCompound, effects);
		return stack;
	}

	public static List<PotionEffect> getEffectsFromStack(ItemStack stack) {
		return getEffectsFromNBT(stack.stackTagCompound);
	}

	/*-------------------------------------------------------- 
	 *                   PLAYER DATA
	 *--------------------------------------------------------*/

	public static void setSoulAmount(EntityPlayer player, float amount) {
		player.getEntityData().setFloat(Ref.NBT.SOUL_AMOUNT, amount);
	}

	public static float getSoulAmount(EntityPlayer player) {
		if (!player.getEntityData().hasKey(Ref.NBT.SOUL_AMOUNT)) {
			player.getEntityData().setFloat(Ref.NBT.SOUL_AMOUNT, 1.0F);
		}
		return player.getEntityData().getFloat(Ref.NBT.SOUL_AMOUNT);
	}

	public static void setDivnity(EntityPlayer player, float divnity) {
		player.getEntityData().setFloat(Ref.NBT.DIVINITY, divnity);
	}

	public static float getDivinity(EntityPlayer player) {
		if (!player.getEntityData().hasKey(Ref.NBT.DIVINITY)) {
			player.getEntityData().setFloat(Ref.NBT.DIVINITY, 0.0F);
		}
		return player.getEntityData().getFloat(Ref.NBT.DIVINITY);
	}

}
