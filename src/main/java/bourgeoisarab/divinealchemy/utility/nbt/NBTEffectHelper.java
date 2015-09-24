package bourgeoisarab.divinealchemy.utility.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import bourgeoisarab.divinealchemy.common.fluid.FluidPotion;
import bourgeoisarab.divinealchemy.common.item.ItemBottlePotion;
import bourgeoisarab.divinealchemy.common.item.ItemBucketPotion;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.Ingredients;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.Log;

public class NBTEffectHelper {

	public static void initTagCompound(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
	}

	public static void initTagCompound(FluidStack stack) {
		if (stack.tag == null) {
			stack.tag = new NBTTagCompound();
		}
	}

	public static NBTTagCompound setPropertiesForNBT(NBTTagCompound tag, PotionProperties properties) {
		if (tag == null || properties == null) {
			return null;
		}
		tag.setInteger(NBTNames.PROPERTIES, properties.getMetaValue());
		return tag;
	}

	public static PotionProperties getPropertiesFromNBT(NBTTagCompound tag) {
		if (tag == null) {
			return null;
		}
		return new PotionProperties(tag.getInteger(NBTNames.PROPERTIES));
	}

	public static NBTTagCompound setIngredientsForNBT(NBTTagCompound tag, Ingredients ingredients) {
		if (ingredients == null) {
			return null;
		}
		NBTTagCompound tagIng = new NBTTagCompound();
		int[] ids = new int[ingredients.getIngredients().length];
		for (int i = 0; i < ingredients.getIngredients().length; i++) {
			if (ingredients.getIngredient(i) != null) {
				ids[i] = ingredients.getIngredient(i).id;
			} else {
				ids[i] = -1;
			}
			tagIng.setBoolean(NBTNames.SIDE_EFFECT + i, ingredients.getSide(i));
		}
		tagIng.setIntArray(NBTNames.INGREDIENTS, ids);
		tag.setTag(NBTNames.INGREDIENTS_TAG, tagIng);
		return tag;
	}

	public static Ingredients getIngredientsFromNBT(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey(NBTNames.INGREDIENTS_TAG)) {
			return null;
		}
		NBTTagCompound tagIng = tag.getCompoundTag(NBTNames.INGREDIENTS_TAG);
		int[] ids = tagIng.getIntArray(NBTNames.INGREDIENTS);
		Ingredients ingredients = new Ingredients();
		for (int i = 0; i < ingredients.getIngredients().length; i++) {
			boolean side = tagIng.getBoolean(NBTNames.SIDE_EFFECT + i);
			if (ids[i] >= 0) {
				ingredients.set(i, PotionIngredient.ingredients.get(ids[i]), side);
			} else {
				ingredients.set(i, null, false);
			}
		}
		return ingredients;
	}

	public static NBTTagCompound setEffectsForNBT(NBTTagCompound tag, Effects effects) {
		if (tag == null || effects == null) {
			return null;
		}

		NBTTagCompound tagEffects = new NBTTagCompound();

		for (int i = 0; i < effects.size(); i++) {
			PotionEffect e = effects.getEffect(i);
			if (e != null) {
				tagEffects.setIntArray(NBTNames.EFFECT + i, new int[]{e.getPotionID(), e.getDuration(), e.getAmplifier()});
				tagEffects.setBoolean(NBTNames.SIDE_EFFECT + i, effects.getSideEffect(i));
			}
		}
		tag.setTag(NBTNames.EFFECTS_TAG, tagEffects);
		return tag;
	}

	public static Effects getEffectsFromNBT(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey(NBTNames.EFFECTS_TAG)) {
			return null;
		}
		Effects effects = new Effects();
		NBTTagCompound tagEffects = tag.getCompoundTag(NBTNames.EFFECTS_TAG);

		for (int i = 0; i < ConfigHandler.maxEffects; i++) {
			if (tagEffects.hasKey(NBTNames.EFFECT + i)) {
				int[] e = tagEffects.getIntArray(NBTNames.EFFECT + i);
				effects.add(new PotionEffect(e[0], e[1], e[2]), tagEffects.getBoolean(NBTNames.SIDE_EFFECT + i));
			} else {
				break;
			}
		}
		return effects;
	}

	public static NBTTagCompound setInstabilityForNBT(NBTTagCompound tag, float instability) {
		if (tag == null) {
			return null;
		}
		tag.setFloat(NBTNames.INSTABILITY, instability);
		return tag;
	}

	public static float getInstabilityFromNBT(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey(NBTNames.INSTABILITY)) {
			return 0;
		}
		return tag.getFloat(NBTNames.INSTABILITY);
	}

	// public static ItemStack setInstabilityForStack(ItemStack stack, float instability) {
	// if (stack == null) {
	// return null;
	// }
	// initTagCompound(stack);
	// setInstabilityForNBT(stack.stackTagCompound, instability);
	// return stack;
	// }
	//
	// public static float getInstabilityFromStack(ItemStack stack) {
	// if (stack == null) {
	// return 0;
	// }
	// return getInstabilityFromNBT(stack.stackTagCompound);
	// }

	public static ItemStack setEffectsForStack(ItemStack stack, Effects effects) {
		if (stack == null || effects == null) {
			return null;
		}

		if (!(stack.getItem() instanceof ItemBucketPotion || stack.getItem() instanceof ItemBottlePotion)) {
			Log.warn("Tried to set potion NBT data for invalid itemstack.");
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			FluidStack fluid = setEffectsForFluid(((IFluidContainerItem) stack.getItem()).getFluid(stack), effects);
			if (fluid != null) {
				fluid.writeToNBT(stack.stackTagCompound);
			}
		} else {
			initTagCompound(stack);
			setEffectsForNBT(stack.stackTagCompound, effects);
		}
		return stack;
	}

	public static Effects getEffectsFromStack(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			return getEffectsFromFluid(((IFluidContainerItem) stack.getItem()).getFluid(stack));
		}
		return getEffectsFromNBT(stack.stackTagCompound);
	}

	public static FluidStack setEffectsForFluid(FluidStack fluid, Effects effects) {
		if (fluid == null) {
			return null;
		}
		if (!(fluid.getFluid() instanceof FluidPotion)) {
			Log.warn("Tried to set potion NBT data for invalid fluidstack.");
		}
		initTagCompound(fluid);
		setEffectsForNBT(fluid.tag, effects);
		return fluid;
	}

	public static Effects getEffectsFromFluid(FluidStack fluid) {
		if (fluid == null) {
			return null;
		}
		return getEffectsFromNBT(fluid.tag);
	}

	public static NBTTagCompound setColouringForNBT(NBTTagCompound tag, Colouring colouring) {
		if (tag == null || colouring == null) {
			return null;
		}
		tag.setIntArray(NBTNames.COLOURS, colouring.getColourArray());
		return tag;
	}

	public static Colouring getColouringFromNBT(NBTTagCompound tag) {
		if (tag == null) {
			return null;
		}
		int[] array = tag.getIntArray(NBTNames.COLOURS);
		if (array == null) {
			return null;
		}
		return new Colouring().setColours(array);
	}

	public static ItemStack setColouringForStack(ItemStack stack, Colouring colouring) {
		if (stack == null || colouring == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			FluidStack fluid = setColouringForFluid(((IFluidContainerItem) stack.getItem()).getFluid(stack), colouring);
			if (fluid != null) {
				fluid.writeToNBT(stack.stackTagCompound);
			}
		} else {
			initTagCompound(stack);
			setColouringForNBT(stack.stackTagCompound, colouring);
		}
		return stack;
	}

	public static Colouring getColouringFromStack(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			return getColouringFromFluid(((IFluidContainerItem) stack.getItem()).getFluid(stack));
		}
		return getColouringFromNBT(stack.stackTagCompound);
	}

	public static FluidStack setColouringForFluid(FluidStack fluid, Colouring colouring) {
		if (fluid == null || colouring == null) {
			return null;
		}
		setColouringForNBT(fluid.tag, colouring);
		return fluid;
	}

	public static Colouring getColouringFromFluid(FluidStack stack) {
		if (stack == null) {
			return null;
		}
		return getColouringFromNBT(stack.tag);
	}

	public static boolean getHiddenFoodEffects(ItemStack stack) {
		if (stack == null || stack.stackTagCompound == null) {
			return false;
		}
		return stack.stackTagCompound.getBoolean(NBTNames.HIDDEN_EFFECTS);
	}
}
