package bourgeoisarab.divinealchemy.common.block.multiblock;

import net.minecraft.world.World;

public interface IMultiBlock {

	public void notifyMultiblockChange(World world, int x, int y, int z);

}
