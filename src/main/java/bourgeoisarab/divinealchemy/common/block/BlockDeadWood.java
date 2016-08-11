package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class BlockDeadWood extends Block {

	public BlockDeadWood() {
		super(Material.wood);
		setUnlocalizedName("deadWood");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

}
