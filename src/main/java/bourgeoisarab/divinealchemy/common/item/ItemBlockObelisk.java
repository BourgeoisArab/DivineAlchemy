package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.common.block.BlockObelisk;

public class ItemBlockObelisk extends ItemBlock {

	public ItemBlockObelisk(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		newState = newState.withProperty(BlockObelisk.PROPERTY_TYPE, (stack.getItemDamage() & 1) == 1).withProperty(BlockObelisk.PROPERTY_TIER, stack.getItemDamage() >> 2);
		return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + "." + ((stack.getItemDamage() & 1) == 1 ? "receptacle." : "") + "name").trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {
		list.add(/* "\u00a7" + (n) + */"Tier " + ((stack.getItemDamage() >> 2) + 1));
	}
}
