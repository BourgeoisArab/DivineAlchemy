package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.item.ItemPedestalCrystal;
import bourgeoisarab.divinealchemy.common.tileentity.TEPedestal;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowered;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class BlockPedestal extends BlockPowered implements IBrewingMultiblock {

	public BlockPedestal() {
		super(Material.rock);
		setUnlocalizedName("pedestal");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	@Override
	public TEPowered createNewTileEntity(World world, int meta) {
		return new TEPedestal();
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemPedestalCrystal) {
			TEPedestal tile = (TEPedestal) world.getTileEntity(pos);
			if (tile.isItemValidForSlot(0, stack)) {
				tile.setInventorySlotContents(0, stack);
				player.setCurrentItemOrArmor(0, null);
				return true;
			}
		} else if (stack == null) {
			TEPedestal tile = (TEPedestal) world.getTileEntity(pos);
			player.setCurrentItemOrArmor(0, tile.getStackInSlot(0));
			tile.setInventorySlotContents(0, null);
		}
		return false;
	}
}
