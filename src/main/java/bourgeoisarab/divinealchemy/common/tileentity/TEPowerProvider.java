package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import bourgeoisarab.divinealchemy.common.energy.EnergyBuffer;

public abstract class TEPowerProvider extends TEDivineAlchemy implements ITickable {

	public final int searchDistanceSq = 256;

	public List<BlockPos> powerSinks = new ArrayList<BlockPos>();
	protected EnergyBuffer buffer = new EnergyBuffer(1000, 1000);

	@Override
	public void update() {
		if (firstRun) {
			updateList();
			cleanList();
			firstRun = false;
		}
	}

	public int drainEnergy(int amount) {
		return buffer.drain(amount);

	}

	protected void updateList() {
		for (TileEntity tile : worldObj.loadedTileEntityList) {
			BlockPos pos = tile.getPos();
			if (getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= searchDistanceSq) {
				if (tile instanceof TEPowered) {
					powerSinks.add(tile.getPos());
				}
			}
		}
	}

	protected void cleanList() {
		for (BlockPos pos : powerSinks) {
			TileEntity tile = worldObj.getTileEntity(pos);
			if (tile == null || !(tile instanceof TEPowered) || getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) > searchDistanceSq) {
				powerSinks.remove(pos);
			}
		}
	}

	public void removeFromNetwork() {
		for (BlockPos pos : powerSinks) {
			TileEntity tile = worldObj.getTileEntity(pos);
			if (tile != null && tile instanceof TEPowerProvider) {
				((TEPowered) tile).powerSources.remove(getPos());
			}
		}
	}

}
