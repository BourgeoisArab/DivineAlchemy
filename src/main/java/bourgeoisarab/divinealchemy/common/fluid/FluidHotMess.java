package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraftforge.fluids.Fluid;

public class FluidHotMess extends Fluid {

	public FluidHotMess() {
		super("fluidHotMess");
		setDensity(1200);
		setViscosity(1500);
		// setIcons(getBlock().getIcon(0, 0), getBlock().getIcon(2, 0));
	}

}
