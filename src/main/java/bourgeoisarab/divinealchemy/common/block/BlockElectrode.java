package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEElectrode;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockElectrode extends BlockContainer {

	public BlockElectrode() {
		super(Material.iron);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockName("blockElectrode");
		setHardness(1.0F);
		setResistance(2.0F);
		setBlockTextureName(Ref.MODID + ":brick_infused");
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TEElectrode();
	}

}
