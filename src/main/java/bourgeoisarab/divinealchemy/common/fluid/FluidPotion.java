package bourgeoisarab.divinealchemy.common.fluid;

import net.minecraftforge.fluids.Fluid;

public class FluidPotion extends Fluid {

	public FluidPotion() {
		super("fluidPotion");
		setDensity(1);
		setViscosity(1000);
	}

	@Override
	public int getColor() {
		return 0x663300;
	}

}
