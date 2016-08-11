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
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	public static void initTagCompound(FluidStack stack) {
		if (stack.tag == null) {
			stack.tag = new NBTTagCompound();
		}
	}

	public static FluidStack setProperties(FluidStack fluid, PotionProperties properties) {
		if (fluid == null || properties == null) {
			return null;
		}
		initTagCompound(fluid);
		fluid.tag.setInteger(NBTNames.PROPERTIES, properties.getMetaValue());
		return fluid;
	}

	public static PotionProperties getProperties(FluidStack fluid) {
		if (fluid == null) {
			return null;
		}
		return getProperties(fluid.tag);
	}

	public static NBTTagCompound setProperties(NBTTagCompound tag, PotionProperties properties) {
		if (tag == null || properties == null) {
			return null;
		}
		tag.setInteger(NBTNames.PROPERTIES, properties.getMetaValue());
		return tag;
	}

	public static PotionProperties getProperties(NBTTagCompound tag) {
		if (tag == null) {
			return null;
		}
		return new PotionProperties(tag.getInteger(NBTNames.PROPERTIES));
	}

	public static NBTTagCompound setIngredients(NBTTagCompound tag, Ingredients ingredients) {
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

	public static Ingredients getIngredients(NBTTagCompound tag) {
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

	public static NBTTagCompound setEffects(NBTTagCompound tag, Effects effects) {
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

	public static Effects getEffects(NBTTagCompound tag) {
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

	public static NBTTagCompound setInstability(NBTTagCompound tag, float instability) {
		if (tag == null) {
			return null;
		}
		tag.setFloat(NBTNames.INSTABILITY, instability);
		return tag;
	}

	public static float getInstability(NBTTagCompound tag) {
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
	// setInstabilityForNBT(stack.getTagCompound(), instability);
	// return stack;
	// }
	//
	// public static float getInstabilityFromStack(ItemStack stack) {
	// if (stack == null) {
	// return 0;
	// }
	// return getInstabilityFromNBT(stack.getTagCompound());
	// }

	public static ItemStack setEffects(ItemStack stack, Effects effects) {
		if (stack == null || effects == null) {
			return null;
		}

		if (!(stack.getItem() instanceof ItemBucketPotion || stack.getItem() instanceof ItemBottlePotion)) {
			Log.warn("Tried to set potion NBT data for invalid itemstack.");
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			FluidStack fluid = setEffects(((IFluidContainerItem) stack.getItem()).getFluid(stack), effects);
			if (fluid != null) {
				fluid.writeToNBT(stack.getTagCompound());
			}
		} else {
			initTagCompound(stack);
			setEffects(stack.getTagCompound(), effects);
		}
		return stack;
	}

	public static Effects getEffects(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			return getEffects(((IFluidContainerItem) stack.getItem()).getFluid(stack));
		}
		return getEffects(stack.getTagCompound());
	}

	public static FluidStack setEffects(FluidStack fluid, Effects effects) {
		if (fluid == null) {
			return null;
		}
		if (!(fluid.getFluid() instanceof FluidPotion)) {
			Log.warn("Tried to set potion NBT data for invalid fluidstack.");
		}
		initTagCompound(fluid);
		setEffects(fluid.tag, effects);
		return fluid;
	}

	public static Effects getEffects(FluidStack fluid) {
		if (fluid == null) {
			return null;
		}
		return getEffects(fluid.tag);
	}

	public static NBTTagCompound setColouring(NBTTagCompound tag, Colouring colouring) {
		if (tag == null || colouring == null) {
			return null;
		}
		tag.setIntArray(NBTNames.COLOURS, colouring.getColourArray());
		return tag;
	}

	public static Colouring getColouring(NBTTagCompound tag) {
		if (tag == null) {
			return null;
		}
		int[] array = tag.getIntArray(NBTNames.COLOURS);
		if (array == null) {
			return null;
		}
		return new Colouring().setColours(array);
	}

	public static ItemStack setColouring(ItemStack stack, Colouring colouring) {
		if (stack == null || colouring == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			FluidStack fluid = setColouring(((IFluidContainerItem) stack.getItem()).getFluid(stack), colouring);
			if (fluid != null) {
				fluid.writeToNBT(stack.getTagCompound());
			}
		} else {
			initTagCompound(stack);
			setColouring(stack.getTagCompound(), colouring);
		}
		return stack;
	}

	public static Colouring getColouring(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		if (stack.getItem() instanceof IFluidContainerItem) {
			return getColouring(((IFluidContainerItem) stack.getItem()).getFluid(stack));
		}
		return getColouring(stack.getTagCompound());
	}

	public static FluidStack setColouring(FluidStack fluid, Colouring colouring) {
		if (fluid == null || colouring == null) {
			return null;
		}
		setColouring(fluid.tag, colouring);
		return fluid;
	}

	public static Colouring getColouring(FluidStack stack) {
		if (stack == null) {
			return null;
		}
		return getColouring(stack.tag);
	}

	public static boolean getHiddenFoodEffects(ItemStack stack) {
		if (stack == null || stack.getTagCompound() == null) {
			return false;
		}
		return stack.getTagCompound().getBoolean(NBTNames.HIDDEN_EFFECTS);
	}
}
