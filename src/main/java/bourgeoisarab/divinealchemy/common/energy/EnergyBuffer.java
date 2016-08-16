package bourgeoisarab.divinealchemy.common.energy;

import net.minecraft.nbt.NBTTagCompound;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class EnergyBuffer {

	protected int maxEnergy;
	protected int energyLevel;

	public EnergyBuffer(int maxEnergy) {
		this(maxEnergy, 0);
	}

	public EnergyBuffer(int maxEnergy, int energyLevel) {
		this.maxEnergy = maxEnergy;
		this.energyLevel = energyLevel;
	}

	public int drain(int amount) {
		int drained = Math.min(energyLevel, amount);
		energyLevel -= drained;
		return drained;
	}

	public int add(int amount) {
		int added = Math.min(maxEnergy - energyLevel, amount);
		energyLevel += added;
		return added;
	}

	public int getEnergyLevel() {
		return energyLevel;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = Math.max(maxEnergy, 0);
	}

	public boolean isFull() {
		return energyLevel >= maxEnergy;
	}

	public boolean isEmpty() {
		return energyLevel <= 0;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setIntArray(NBTNames.ENERGY_DATA, new int[]{maxEnergy, energyLevel});
		return nbt;
	}

	public static EnergyBuffer readFromNBT(NBTTagCompound nbt) {
		int[] data = nbt.getIntArray(NBTNames.ENERGY_DATA);
		return new EnergyBuffer(data[0], data[1]);
	}

}
