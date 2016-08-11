package bourgeoisarab.divinealchemy.init;

import net.minecraftforge.fluids.FluidRegistry;
import bourgeoisarab.divinealchemy.common.fluid.FluidHotMess;
import bourgeoisarab.divinealchemy.common.fluid.FluidPotion;

public class ModFluids {

	public static FluidPotion potion;
	public static FluidHotMess hotMess;

	public static void init() {
		potion = new FluidPotion();
		hotMess = new FluidHotMess();
	}

	public static void register() {
		FluidRegistry.registerFluid(potion);
		FluidRegistry.registerFluid(hotMess);
	}

}
