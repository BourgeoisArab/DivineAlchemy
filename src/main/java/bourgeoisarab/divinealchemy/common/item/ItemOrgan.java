package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;

public class ItemOrgan extends ItemFood {

	public final String[] organs = new String[]{"heart", "lungs", "kidneys", "intestines", "liver", "brain", "stomach"};
	public final String[] types = new String[]{"item.organ.human", "item.organ.animal", "item.organ.zombie"};

	public ItemOrgan() {
		super(2, true);
		setUnlocalizedName("organ");
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(DivineAlchemy.tabDivineAlchemy);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int i) {
		// Darkens colour of zombie meat
		return getType(stack.getItemDamage()) != types.length - 1 ? 0xFFFFFF : 0x88DD88;
	}

	public int getType(int meta) {
		return meta % types.length;
	}

	public int getOrgan(int meta) {
		return (meta - getType(meta)) / types.length;
	}

	public int getMetaValue(int organ, int type) {
		return organ * types.length + type;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(I18n.format(types[getType(stack.getItemDamage())]));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		int organ = getOrgan(stack.getItemDamage());
		if (organ >= organs.length) {
			return null;
		}
		return StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + "." + organs[organ] + ".name").trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < organs.length * types.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean currentItem) {

	}

}
