package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class FluidPotion extends Fluid {

	public static ResourceLocation still = new ResourceLocation(Ref.MODID + ":blocks/potion_still");
	public static ResourceLocation flowing = new ResourceLocation(Ref.MODID + ":blocks/potion_flowing");

	public FluidPotion() {
		super("potion", still, flowing);
		setDensity(1500);
		setViscosity(1500);
	}

	@Override
	public int getColor() {
		return ColourHelper.potionColourInt;
	}

	@Override
	public int getColor(FluidStack fluid) {
		Effects effects = NBTEffectHelper.getEffects(fluid);
		if (effects != null) {
			return ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), NBTEffectHelper.getColouring(fluid)));
		}
		return getColor();
	}

}
