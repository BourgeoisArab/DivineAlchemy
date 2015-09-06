package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.reference.Ref;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOrgan extends ItemFood {

	protected String[] organs = new String[]{"heart", "lungs", "kidneys", "intestines", "liver", "brain", "stomach", "pancreas"};
	protected IIcon[] icons = new IIcon[organs.length];
	protected String[] types = new String[]{I18n.format("item.organ.human"), I18n.format("item.organ.pig"), I18n.format("item.organ.cow"), I18n.format("item.organ.sheep"), I18n.format("item.organ.zombie")};

	public ItemOrgan() {
		super(3, true);
		setUnlocalizedName("itemOrgan");
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(DivineAlchemy.tabDivineAlchemy);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(Ref.Location.PREFIX + "heart");
		for (int i = 0; i < icons.length; i++) {
			icons[i] = register.registerIcon(Ref.Location.PREFIX + organs[i].toLowerCase());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[getOrgan(meta)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int i) {
		// Darkens colour of zombie meat
		return getType(stack.getItemDamage()) != types.length - 1 ? 0xFFFFFF : 0x77FF77;
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
		list.add(types[getType(stack.getItemDamage())]);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(stack) + "." + organs[getOrgan(stack.getItemDamage())] + ".name")).trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < organs.length * types.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
