package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class ItemPedestalCrystal extends Item {

	public static enum TYPE {
		AMPLIFIER, DURATION, INSTABILITY;
	}

	public ItemPedestalCrystal() {
		setUnlocalizedName("pedestalCrystal");
		setMaxStackSize(1);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	public TYPE getType(ItemStack stack) {
		return TYPE.values()[stack.getItemDamage() & 3];
	}

	public int getTier(ItemStack stack) {
		return stack.getItemDamage() >> 2;
	}

	public int getMeta(TYPE type, int tier) {
		return (tier << 2) + type.ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("Tier " + getTier(stack));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + "." + getType(stack).name() + ".name")).trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(item, 1, getMeta(TYPE.AMPLIFIER, 1)));
		subItems.add(new ItemStack(item, 1, getMeta(TYPE.DURATION, 1)));
		subItems.add(new ItemStack(item, 1, getMeta(TYPE.INSTABILITY, 1)));
	}

}
