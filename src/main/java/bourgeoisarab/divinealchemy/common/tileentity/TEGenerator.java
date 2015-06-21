package bourgeoisarab.divinealchemy.common.tileentity;

import bourgeoisarab.divinealchemy.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;


public class TEGenerator extends TileEntity implements IEnergyHandler {
	
	private final int energyCapacity = 10000;
	private int energyLevel = 0;

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		if (energyLevel + maxReceive <= energyCapacity) {
			if (!simulate) energyLevel += maxReceive;
			return maxReceive;
		} else {
			int received = energyCapacity - energyLevel;
			if (!simulate) energyLevel = energyCapacity;
			return received;
		}
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (maxExtract <= energyLevel) {
			if (!simulate) energyLevel -= maxExtract;
			return maxExtract;
		} else {
			int extracted = energyLevel;
			if (!simulate) energyLevel = 0;
			return extracted;
		}
	}
 
	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energyLevel;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyCapacity;
	}
	
	@Override
	public void updateEntity()
	{
		if (worldObj.getWorldTime() % 10 == 0)
		{
			Block blockNorth = worldObj.getBlock(xCoord + 1, yCoord, zCoord);
			Block blockSouth = worldObj.getBlock(xCoord - 1, yCoord, zCoord);
			Block blockEast = worldObj.getBlock(xCoord, yCoord, zCoord + 1);
			Block blockWest = worldObj.getBlock(xCoord, yCoord, zCoord - 1);
			if ((blockNorth == ModBlocks.blockElectrode && blockSouth == ModBlocks.blockElectrode)
					|| (blockEast == ModBlocks.blockElectrode && blockWest == ModBlocks.blockElectrode)) {
				if (energyLevel + 10 <= energyCapacity) {
					energyLevel += 10;
					System.out.println(energyLevel);
				}
			}
		}
	}

}
