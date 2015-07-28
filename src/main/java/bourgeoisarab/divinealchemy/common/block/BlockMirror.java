package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMirror extends Block {

	public BlockMirror() {
		super(Material.rock);
		setBlockName("blockMirror");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

}
