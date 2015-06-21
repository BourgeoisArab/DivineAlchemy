package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class TEItemReceptacle extends TileEntity implements IFluidTank {
	
	private ItemStack item;
	private int fluidLevel = 0;
	private FluidTank tank;
	
	public TEItemReceptacle() {
		this.tank = new FluidTank(FluidRegistry.WATER, 0, 1000);
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack stack) {
		this.item = stack;
	}
	
	public int getFluidLevel() {
		return fluidLevel;
	}

	@Override
	public FluidStack getFluid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFluidAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidTankInfo getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

}
