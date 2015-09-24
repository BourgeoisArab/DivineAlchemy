package bourgeoisarab.divinealchemy.common.block.multiblock;

import net.minecraft.block.material.Material;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;

public class BlockInfusedBrick extends BlockMultiBlock {

	public BlockInfusedBrick() {
		super(Material.rock);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setBlockName("infusedBrick");
		setBlockTextureName(Ref.MODID + ":brick_infused");
		setHardness(10.0F);
		setResistance(1750.0F);
	}

}
