package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class ItemPotionFood extends ItemFood {

	public ItemPotionFood() {
		super(0, false);
		setUnlocalizedName("itemPotionFood");
		setTextureName("minecraft:bread");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Ref.NBT.FOOD_ID)) {
			return Item.getItemById(stack.stackTagCompound.getInteger(Ref.NBT.FOOD_ID)).getIcon(stack, pass);
		}
		return super.getIcon(stack, pass);
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Ref.NBT.FOOD_ID)) {
			return Item.getItemById(stack.stackTagCompound.getInteger(Ref.NBT.FOOD_ID)).getIconIndex(stack);
		}
		return super.getIconIndex(stack);
	}

	@Override
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
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		if (PotionProperties.getSplash(stack.getItemDamage())) {
			list.add("Splash");
		}
		if (PotionProperties.getBlessed(stack.getItemDamage())) {
			list.add("Blessed");
		}
		if (PotionProperties.getBlessed(stack.getItemDamage())) {
			list.add("Cursed");
		}
		Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
				String s1 = I18n.format(potion.getName());

				s1 = s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1));

				list.add((effects.getSideEffect(i) ? "§4" : "§7") + s1 + " (" + Potion.getDurationString(effects.getEffect(i)) + ")");
			}
		}
	}

}
