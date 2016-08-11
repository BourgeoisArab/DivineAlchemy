package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class BlockDeadDirt extends Block {

	public BlockDeadDirt() {
		super(Material.ground);
		setUnlocalizedName("deadDirt");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

}
