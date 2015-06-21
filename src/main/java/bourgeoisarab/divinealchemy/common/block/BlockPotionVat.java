package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionVat;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPotionVat extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockPotionVat() {
		super(Material.iron);
		setBlockName("blockPotionVat");
		setHardness(1.0F);
		setResistance(2.0F);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockTextureName(Ref.MODID + ":brick_infused");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEPotionVat();
	}

	// @Override
	// public void registerBlockIcons(IIconRegister register) {
	// blockIcon = register.registerIcon("brick_infused");
	// // for (int i = 0; i < 5; i++) {
	// // icons[i] = register.registerIcon("potion_vat_" + i);
	// // }
	// }

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);

		if ((meta == 0 && side == 2) || (meta == 1 && side == 3) || (meta == 2 && side == 4) || (meta == 3 && side == 5)) {
			return icons[((TEPotionVat) world.getTileEntity(x, y, z)).getTier()];
		} else {
			return blockIcon;
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	// @Override
	// public void getSubBlocks(Item item, CreativeTabs tab, List list) {
	// for (int i = 0; i < 5; i++) {
	// list.add(new ItemStack(item, 1, i));
	// }
	// }

}
