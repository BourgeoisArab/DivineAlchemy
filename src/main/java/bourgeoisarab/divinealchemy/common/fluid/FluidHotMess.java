package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraftforge.fluids.Fluid;

public class FluidHotMess extends Fluid {

	public FluidHotMess() {
		super("fluidHotMess");
		setDensity(1200);
		setViscosity(1500);
	}

	@Override
	public int getColor() {
		return 0xAA00AA;
	}

}
