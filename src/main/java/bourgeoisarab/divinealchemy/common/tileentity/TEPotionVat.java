package bourgeoisarab.divinealchemy.common.tileentity;

import bourgeoisarab.divinealchemy.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class TEPotionVat extends TEPotionVatBase {

	private int energy;
	
	@Override
	public int getEnergyLevel() {
		return this.energy;
	}
	
	public int extractEnergy(ForgeDirection dir, int amount, boolean simulate) {
		int extracted = Math.min(energy, amount);
		if (!simulate) energy -= extracted;
		return extracted;
	}
	
	public int receiveEnergy(ForgeDirection dir, int amount, boolean simulate) {
		int received = Math.min(capacity - amount, amount);
		if (!simulate) energy += received;
		return received;
	}
	
	public int getMaxEnergyStored(ForgeDirection dir) {
		return this.capacity;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
		if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == ModBlocks.blockGenerator) {
			
		}
	}

}
