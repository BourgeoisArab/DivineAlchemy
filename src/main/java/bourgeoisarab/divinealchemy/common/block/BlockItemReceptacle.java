package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEItemReceptacle;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockItemReceptacle extends BlockContainer {

	private IIcon topIcon;

	public BlockItemReceptacle() {
		super(Material.rock);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setBlockName("blockItemReceptacle");
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
		topIcon = register.registerIcon(Ref.MODID + ":receptacle_top");
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

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int baa, float fuuuu, float begegekl, float rfg) {
		if (player.isSneaking()) {
			return false;
		}

		ItemStack item = player.getCurrentEquippedItem();
		TEItemReceptacle tile = (TEItemReceptacle) world.getTileEntity(x, y, z);

		if (item != null && tile.getItem() == null) {
			if (!player.capabilities.isCreativeMode) item.stackSize--;
			tile.setItem(new ItemStack(item.getItem(), 1, item.getItemDamage()));
			return true;
		}
		if (tile.getItem() != null) {
			if (item == null) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getItem());
				tile.setItem(null);
			} else if (item.stackSize == 1) {
				ItemStack stack = tile.getItem();
				tile.setItem(item);
				player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
			}
			return true;
		}
		return false;
	}
}
