package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.ItemFluidContainer;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBottlePotion extends ItemFluidContainer {

	@SideOnly(Side.CLIENT)
	public IIcon splashBottle;
	@SideOnly(Side.CLIENT)
	public IIcon overlay;

	public ItemBottlePotion() {
		super(0, FluidContainerRegistry.BUCKET_VOLUME);
		setUnlocalizedName("potionBottle");
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
			Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
			if (effects == null) {
				return ColourHelper.potionColourInt;
			}
			Colouring colours = NBTEffectHelper.getColouringFromStack(stack);
			int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), colours));
			return colour == -1 ? ColourHelper.potionColourInt : colour;
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
		int uses = stack.stackTagCompound != null ? stack.stackTagCompound.getInteger(NBTNames.REMAINING_USES) : 0;
		list.add(uses == 0 ? I18n.format("item.potion.empty") : I18n.format("item.potion.uses"));
		PotionProperties properties = NBTEffectHelper.getPropertiesFromNBT(getFluid(stack).tag);
		if (properties.isSplash) {
			list.add(I18n.format("item.potion.splash"));
		}
		if (properties.isBlessed) {
			list.add(I18n.format("item.potion.blessed"));
		}
		if (properties.isCursed) {
			list.add(I18n.format("item.potion.cursed"));
		}
		Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
				String s1 = I18n.format(potion.getName());

				s1 = s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1));

				list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + (potion.isInstant() ? "" : " (" + Potion.getDurationString(effects.getEffect(i)) + ")"));
			}
		}
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		int usesLeft = stack.getItemDamage();
		Effects e = NBTEffectHelper.getEffectsFromStack(stack);
		if (e == null) {
			return stack;
		}
		List<PotionEffect> effects = NBTEffectHelper.getEffectsFromStack(stack).getEffects();

		ModPotionHelper.addEffectsToEntity(effects, player);

		if (PotionProperties.getPersistent(stack.getItemDamage())) {
			if (!player.getEntityData().hasKey("AI.PersistentIDs")) {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.potionsToIntArray(effects)[0]);
			} else {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.mergeIntArrays(player.getEntityData().getIntArray("AI.PersistentIDs"), ModPotionHelper.potionsToIntArray(effects)[0]));
			}
		}
		if (!player.capabilities.isCreativeMode) {
			stack.setItemDamage(usesLeft - 1);
		}
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
		return effects != null ? effects.size() * 16 : 16;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() > 0 && !NBTEffectHelper.getPropertiesFromNBT(getFluid(stack).tag).isSplash) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		} else {
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntitySplashPotion(world, player, NBTEffectHelper.getEffectsFromStack(stack)));
			}
		}
		return stack;
	}

}
