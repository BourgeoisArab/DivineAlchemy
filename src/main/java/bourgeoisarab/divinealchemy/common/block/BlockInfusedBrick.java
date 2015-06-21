package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInfusedBrick extends Block {

	public BlockInfusedBrick() {
		super(Material.rock);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockName("blockInfusedBrick");
		setBlockTextureName(Ref.MODID + ":brick_infused");
		setHardness(10.0F);
		setResistance(1750.0F);
	}

}
