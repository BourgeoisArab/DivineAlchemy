package bourgeoisarab.divinealchemy.init;

import bourgeoisarab.divinealchemy.common.fluid.FluidHotMess;
import bourgeoisarab.divinealchemy.common.fluid.FluidPotion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static Fluid potion;
	public static Fluid hotMess;

	public static void init() {
		potion = new FluidPotion();
		hotMess = new FluidHotMess();
	}

	public static void register() {
		FluidRegistry.registerFluid(potion);
		FluidRegistry.registerFluid(hotMess);
	}

}
