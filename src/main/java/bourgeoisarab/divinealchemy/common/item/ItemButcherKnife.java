package bourgeoisarab.divinealchemy.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class ItemButcherKnife extends ItemSword {

	public ItemButcherKnife() {
		super(ToolMaterial.IRON);
		setUnlocalizedName("butcherKnife");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setMaxDamage(192);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 0;
	}

	// @Override
	// public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
	// return 1.0F;
	// }

	// @Override
	// public boolean func_150897_b(Block block) {
	// return false;
	// }

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		return stack;
	}
}
