package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class FluidPotion extends Fluid {

	public FluidPotion() {
		super("fluidPotion");
		setDensity(1500);
		setViscosity(1500);
	}

	@Override
	public int getColor() {
		return ColourHelper.potionColourInt;
	}

	@Override
	public int getColor(FluidStack fluid) {
		Effects effects = NBTEffectHelper.getEffectsFromFluid(fluid);
		if (effects != null) {
			return ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), NBTEffectHelper.getColouringFromFluid(fluid)));
		}
		return getColor();
	}

}
