package bourgeoisarab.divinealchemy.init;

import bourgeoisarab.divinealchemy.common.fluid.FluidHotMess;
import bourgeoisarab.divinealchemy.common.fluid.FluidPotion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static Fluid fluidPotion;
	public static Fluid fluidHotMess;

	public static void init() {
		fluidPotion = new FluidPotion();
		fluidHotMess = new FluidHotMess();
	}

	public static void register() {
		FluidRegistry.registerFluid(fluidPotion);
		FluidRegistry.registerFluid(fluidHotMess);
	}

}
