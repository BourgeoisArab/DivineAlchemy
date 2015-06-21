package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEItemReceptacle;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockChannel extends BlockContainer {

	private IIcon topIcon;

	public BlockChannel() {
		super(Material.rock);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockName("blockChannel");
		setBlockTextureName(Ref.MODID + ":brick_infused");
		setHardness(1.0F);
		setResistance(1000.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEItemReceptacle();
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(Ref.MODID + ":brick_infused");
		topIcon = register.registerIcon(Ref.MODID + ":channel_top");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? topIcon : blockIcon;
	}

	@Override
	public boolean isOpaqueCube() {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public int getRenderType() {
		return 0;
	}

}
