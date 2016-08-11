package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import bourgeoisarab.divinealchemy.reference.Ref;

public class FluidHotMess extends Fluid {

	public static ResourceLocation still = new ResourceLocation(Ref.MODID + ":blocks/hotmess_still");
	public static ResourceLocation flowing = new ResourceLocation(Ref.MODID + ":blocks/hotmess_flowing");

	public FluidHotMess() {
		super("hotmess", still, flowing);
		setDensity(1200);
		setViscosity(1500);
	}

	@Override
	public int getColor() {
		return 0xFFFFFF;
	}

}
