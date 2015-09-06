package bourgeoisarab.divinealchemy.common.block.multiblock;

import net.minecraft.block.material.Material;

public class BlockInfusedGlass extends BlockMultiBlock {

	public BlockInfusedGlass() {
		super(Material.rock);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

}
