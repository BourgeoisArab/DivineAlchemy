package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;

public class TEPotionVatCoFH extends TEPotionVatBase implements IEnergyHandler, IEnergyConnection {

	private EnergyStorage storage = new EnergyStorage(this.capacity);
	
	@Override
	public int getEnergyLevel() {
		return this.getEnergyStored(ForgeDirection.UP);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection dir) {
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection dir, int amount, boolean simulate) {
		return this.storage.extractEnergy(amount, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection dir) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection dir) {
		return this.storage.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(ForgeDirection dir, int amount, boolean simulate) {
		return this.storage.receiveEnergy(amount, simulate);
	}

}
