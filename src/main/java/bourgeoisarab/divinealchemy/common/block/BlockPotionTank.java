package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.utility.InventoryHelper;

public class BlockPotionTank extends BlockContainer {

	public BlockPotionTank() {
		super(Material.rock);
		setUnlocalizedName("potionTank");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setHardness(1.0F);
		setResistance(2.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEPotionTank();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack == null) {
			return false;
		}
		TEPotionTank tile = (TEPotionTank) world.getTileEntity(pos);
		ItemStack newStack = InventoryHelper.processContainer(stack, tile, side);
		if (tile.tank.getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME == 1) {
			tile.tank.getFluid().amount--;
		} else if (tile.tank.getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME == 999) {
			tile.tank.getFluid().amount++;
		}
		InventoryHelper.addStackToInventory(player, stack, newStack, true);
		return stack != newStack;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

}
