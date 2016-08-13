package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class ItemAmulet extends Item {

	public ItemAmulet() {
		setUnlocalizedName("amulet");
		setMaxStackSize(1);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return stack.getTagCompound().getShort(NBTNames.MAX_DAMAGE);
	}

	public ItemStack setEffect(ItemStack stack, int id, int amplifier) {
		if (stack.getTagCompound() == null) {
			initNBTData(stack);
		}
		stack.getTagCompound().setIntArray(NBTNames.EFFECT, new int[]{id, amplifier});
		return stack;
	}

	public ItemStack initNBTData(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setIntArray(NBTNames.EFFECT, new int[]{0, 0});
		stack.getTagCompound().setBoolean(NBTNames.ACTIVE, false);
		stack.getTagCompound().setShort(NBTNames.COOLDOWN, (short) 0);
		stack.getTagCompound().setShort(NBTNames.MAX_COOLDOWN, (short) 60);
		stack.getTagCompound().setShort(NBTNames.MAX_DAMAGE, (short) 18000);
		return stack;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		int[] data = stack.getTagCompound().getIntArray(NBTNames.EFFECT);
		boolean active = stack.getTagCompound().getBoolean(NBTNames.ACTIVE);
		Potion potion = ModPotion.getPotion(data[0]);
		if (!potion.isInstant() && !active) {
			stack.getTagCompound().setBoolean(NBTNames.ACTIVE, true);
			stack.damageItem(10, player);
		} else if (!potion.isInstant()) {
			stack.getTagCompound().setBoolean(NBTNames.ACTIVE, false);
		} else {
			// if (potion instanceof ISplashEffect) {
			potion.performEffect(player, data[1]);
			// } else if (!world.isRemote) {
			// world.spawnEntityInWorld(new EntityAmuletSpell(world, player, data[0], data[1]));
			// }
			stack.getTagCompound().setShort(NBTNames.COOLDOWN, stack.getTagCompound().getShort(NBTNames.MAX_COOLDOWN));
			stack.damageItem(1, player);
		}
		return stack;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		super.onUpdate(stack, world, entity, slot, isSelected);
		if (entity instanceof EntityLivingBase && stack.getTagCompound() != null) {
			if (stack.getTagCompound().getBoolean(NBTNames.ACTIVE)) {
				int[] data = stack.getTagCompound().getIntArray(NBTNames.EFFECT);
				PotionEffect e = new PotionEffect(data[0], 20, data[1]);
				if (world.isRemote) {
					e.setPotionDurationMax(false);
				}
				((EntityLivingBase) entity).addPotionEffect(e);
				stack.damageItem(1, (EntityLivingBase) entity);
			} else {
				short cooldown = stack.getTagCompound().getShort(NBTNames.COOLDOWN);
				if (cooldown > 0) {
					stack.getTagCompound().setShort(NBTNames.COOLDOWN, cooldown--);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if (stack.getTagCompound() == null) {
			return 0xFFFFFF;
		}
		int[] data = stack.getTagCompound().getIntArray(NBTNames.EFFECT);
		Potion p = ModPotion.getPotion(data[0]);
		return p != null ? p.getLiquidColor() : 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			return false;
		}
		return stack.getTagCompound().getBoolean(NBTNames.ACTIVE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {
		if (stack.getTagCompound() == null) {
			return;
		}
		int[] data = stack.getTagCompound().getIntArray(NBTNames.EFFECT);
		Potion p = ModPotion.getPotion(data[0]);
		if (p != null) {
			list.add(I18n.format(p.getName()) + " " + I18n.format("enchantment.level." + data[1]));
			if (p.isInstant()) {
				int cooldown = stack.getTagCompound().getShort(NBTNames.COOLDOWN);
				if (cooldown > 0) {
					list.add("Cooldown: " + cooldown);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(setEffect(new ItemStack(item, 1, 0), Potion.heal.id, 5));
	}

}
