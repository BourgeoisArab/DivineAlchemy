package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.NBTHelper;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionFood extends ItemFood {

	public ItemPotionFood() {
		super(0, false);
		setUnlocalizedName("itemPotionFood");
		setTextureName("minecraft:bread");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Ref.NBT.FOOD_ID)) {
			return Item.getItemById(stack.stackTagCompound.getInteger(Ref.NBT.FOOD_ID)).getIcon(stack, pass);
		} else
			return super.getIcon(stack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Ref.NBT.FOOD_ID)) {
			return Item.getItemById(stack.stackTagCompound.getInteger(Ref.NBT.FOOD_ID)).getIconIndex(stack);
		} else
			return super.getIconIndex(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Ref.NBT.EFFECTS_TAG);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		itemIcon = getIconIndex(stack);
		// TODO: Don't cheat here
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		--stack.stackSize;
		NBTTagCompound tag = stack.stackTagCompound;
		if (tag != null) {
			player.getFoodStats().addStats(tag.getInteger(Ref.NBT.FOOD_LEVEL), tag.getFloat(Ref.NBT.FOOD_SATURATION));
		}
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		onFoodEaten(stack, world, player);
		return stack;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		super.onFoodEaten(stack, world, player);

		// List<PotionEffect> effects =
		// ModPotionHelper.getEffectsFromStack(stack);
		//
		// if (effects != null) {
		// for (int i = 0; i < effects.size(); i++) {
		// player.addPotionEffect(new PotionEffect(effects.get(i).getPotionID(),
		// effects.get(i).getDuration(), effects.get(i).getAmplifier()));
		// }
		// }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		List<PotionEffect> effects = NBTHelper.getEffectsFromStack(stack);

		if (effects != null) {
			for (PotionEffect i : effects) {
				Potion potion = Potion.potionTypes[i.getPotionID()];
				String s1 = I18n.format(potion.getName(), new Object[0]);

				s1 = s1 + " " + I18n.format("enchantment.level." + (i.getAmplifier() + 1), new Object[0]);

				list.add(s1 + " (" + Potion.getDurationString(i) + ")");
			}
		}
	}

}
