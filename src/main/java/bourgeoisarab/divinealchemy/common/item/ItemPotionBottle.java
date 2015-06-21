package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.NBTHelper;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBottle extends ItemFluidContainer {

	@SideOnly(Side.CLIENT)
	public IIcon splashBottle;
	@SideOnly(Side.CLIENT)
	public IIcon overlay;

	public ItemPotionBottle() {
		super(0, 333);
		setUnlocalizedName("itemPotionBottle");
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(Ref.MODID + ":bottle_base");
		splashBottle = register.registerIcon(Ref.MODID + ":bottle_splash_base");
		overlay = register.registerIcon(Ref.MODID + ":bottle_overlay");
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(NBTHelper.getEffectsFromStack(stack)));
			return colour == -1 ? ColourHelper.combineColours(ColourHelper.potionColour) : colour;
		}
		return 0xFFFFFF;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
		return pass == 0 ? overlay : meta == 0 ? itemIcon : splashBottle;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		List<PotionEffect> effects = NBTHelper.getEffectsFromStack(stack);

		if (effects != null) {
			for (PotionEffect i : effects) {
				Potion potion = Potion.potionTypes[i.getPotionID()];
				String s1 = I18n.format(potion.getName());

				s1 = s1 + " " + I18n.format("enchantment.level." + (i.getAmplifier() + 1));

				list.add(s1 + " (" + Potion.getDurationString(i) + ")");
			}
		}
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		List<PotionEffect> effects = NBTHelper.getEffectsFromStack(stack);

		ModPotionHelper.addEffectsToEntity(effects, player);

		if (stack.stackTagCompound.hasKey("AI.Persistent") && stack.stackTagCompound.getBoolean("AI.Persistent")) {
			if (!player.getEntityData().hasKey("AI.PersistentIDs")) {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.potionsToIntArray(effects)[0]);
			} else {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.mergeIntArrays(player.getEntityData().getIntArray("AI.PersistentIDs"), ModPotionHelper.potionsToIntArray(effects)[0]));
			}
		}
		return player.capabilities.isCreativeMode ? stack : new ItemStack(Items.glass_bottle);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stacks) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() == 0) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		} else {
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntitySplashPotion(world, player, NBTHelper.getEffectsFromStack(stack)));
			}
		}
		return stack;
	}

}
