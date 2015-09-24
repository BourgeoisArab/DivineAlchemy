package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TEPotionTank extends TileEntityBaseDA implements IFluidHandler {

	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 8);

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource != null && tank.getFluid() != null && resource.getFluid() == tank.getFluid().getFluid()) {
			return drain(from, resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return tank.getFluid() != null && tank.getFluid().getFluid() == fluid;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

}
