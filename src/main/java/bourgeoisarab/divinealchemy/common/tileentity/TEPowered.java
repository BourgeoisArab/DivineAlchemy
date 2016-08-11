package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import bourgeoisarab.divinealchemy.common.energy.EnergyBuffer;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public abstract class TEPowered extends TEDivineAlchemy implements ITickable {

	public final int updateRate = 20;
	public final int searchDistanceSq = 256;

	public List<BlockPos> powerSources = new ArrayList<BlockPos>();

	// Energy buffers
	protected EnergyBuffer buffer = new EnergyBuffer(1000);
	protected int energyFlow = 100;

	@Override
	public void update() {
		if (firstRun) {
			updateList();
			cleanList();
			firstRun = false;
		}
		if (worldObj.getWorldTime() % updateRate == 0) {
			if (isRunning() && !buffer.isFull()) {
				requestEnergy(energyFlow);
			}
		}
	}

	public abstract boolean isRunning();

	protected void requestEnergy(int amount) {
		for (BlockPos pos : powerSources) {
			TileEntity tile = worldObj.getTileEntity(pos);
			if (tile != null && tile instanceof TEPowerProvider) {
				buffer.add(((TEPowerProvider) tile).drainEnergy(amount));
			}
		}
	}

	protected void updateList() {
		for (TileEntity tile : worldObj.loadedTileEntityList) {
			BlockPos pos = tile.getPos();
			if (getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= searchDistanceSq) {
				if (tile instanceof TEPowerProvider) {
					powerSources.add(tile.getPos());
				}
			}
		}
	}

	protected void cleanList() {
		for (BlockPos pos : powerSources) {
			TileEntity tile = worldObj.getTileEntity(pos);
			if (tile == null || !(tile instanceof TEPowerProvider) || getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) > searchDistanceSq) {
				powerSources.remove(pos);
			}
		}
	}

	public void removeFromNetwork() {
		for (BlockPos pos : powerSources) {
			TileEntity tile = worldObj.getTileEntity(pos);
			if (tile != null && tile instanceof TEPowerProvider) {
				((TEPowerProvider) tile).powerSinks.remove(getPos());
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		buffer = EnergyBuffer.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList(NBTNames.POWER_SOURCES, 4);
		for (int i = 0; i < list.tagCount(); i++) {
			powerSources.add(BlockPos.fromLong(((NBTTagLong) list.get(i)).getLong()));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		buffer.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for (BlockPos pos : powerSources) {
			list.appendTag(new NBTTagLong(pos.toLong()));
		}
		nbt.setTag(NBTNames.POWER_SOURCES, list);
	}

}
