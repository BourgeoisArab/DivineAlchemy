package bourgeoisarab.divinealchemy.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.block.BlockObeliskDark;

public class ItemBlockObeliskDark extends ItemBlock {

	public ItemBlockObeliskDark(Block block) {
		super(block);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState.withProperty(BlockObeliskDark.PROPERTY_TYPE, false));
	}

}
