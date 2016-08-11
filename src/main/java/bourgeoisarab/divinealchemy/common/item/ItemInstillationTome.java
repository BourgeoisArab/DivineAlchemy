package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class ItemInstillationTome extends Item {

	public ItemInstillationTome() {
		super();
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setUnlocalizedName("instillationTome");
		setMaxStackSize(1);
	}

	// @Override
	// @SideOnly(Side.CLIENT)
	// public void registerIcons(IIconRegister iconRegister) {
	// itemIcon = iconRegister.registerIcon(Ref.Location.PREFIX + "instillationtome");
	// }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add("By Libatious Boridge");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		return super.onItemRightClick(stack, world, player);
	}

}
