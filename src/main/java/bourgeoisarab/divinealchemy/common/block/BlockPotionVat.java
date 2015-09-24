package bourgeoisarab.divinealchemy.common.block;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionVat;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPotionVat extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[5];

	public BlockPotionVat() {
		super(Material.iron);
		setBlockName("potionVat");
		setHardness(1.0F);
		setResistance(2.0F);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setBlockTextureName(Ref.Location.PREFIX + "brick_infused");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEPotionVat();
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(Ref.Location.PREFIX + "brick_infused");
		for (int i = 0; i < 5; i++) {
			icons[i] = register.registerIcon(Ref.Location.PREFIX + "potion_vat_" + i);
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TEPotionVat tile = (TEPotionVat) world.getTileEntity(x, y, z);
		return tile.orientation != ForgeDirection.getOrientation(side) ? blockIcon : icons[tile.getTier()];
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 3 && (meta >= 0 || meta < icons.length) ? icons[meta] : blockIcon;
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		TileEntity t = world.getTileEntity(x, y, z);
		if (t instanceof TEPotionVat && stack != null && entity != null) {
			TEPotionVat tile = (TEPotionVat) t;
			float rotation = entity.rotationYaw % 360;
			if (rotation < 0) {
				rotation += 360;
			}
			if (rotation >= 315 || rotation <= 45) {
				tile.orientation = ForgeDirection.NORTH;
			} else if (rotation >= 45 && rotation <= 135) {
				tile.orientation = ForgeDirection.EAST;
			} else if (rotation >= 135 && rotation <= 215) {
				tile.orientation = ForgeDirection.SOUTH;
			} else if (rotation >= 215 && rotation <= 315) {
				tile.orientation = ForgeDirection.WEST;
			} else {
				tile.orientation = ForgeDirection.NORTH;
			}
			world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 3);
			tile.checkMultiBlock();
		}
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 5; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof TEPotionVat) {
			TEPotionVat tile = (TEPotionVat) entity;
			tile.checkMultiBlock();
			return true;
		}
		return false;
	}

}
