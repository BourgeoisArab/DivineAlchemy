package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class ItemBottlePotion extends ItemFluidContainer {

	// @SideOnly(Side.CLIENT)
	// public IIcon splashBottle;
	// @SideOnly(Side.CLIENT)
	// public IIcon overlay;

	public ItemBottlePotion() {
		super(0);
		setCapacity(333);
		setUnlocalizedName("potionBottle");
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	// @Override
	// public void registerIcons(IIconRegister register) {
	// itemIcon = register.registerIcon(Ref.MODID + ":bottle_base");
	// splashBottle = register.registerIcon(Ref.MODID + ":bottle_splash_base");
	// overlay = register.registerIcon(Ref.MODID + ":bottle_overlay");
	// }

	// @Override
	// public boolean requiresMultipleRenderPasses() {
	// return true;
	// }

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			Effects effects = NBTEffectHelper.getEffects(stack);
			if (effects == null) {
				return ColourHelper.potionColourInt;
			}
			Colouring colours = NBTEffectHelper.getColouring(stack);
			int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), colours));
			return colour == -1 ? ColourHelper.potionColourInt : colour;
		}
		return 0xFFFFFF;
	}

	// @Override
	// public IIcon getIcon(ItemStack stack, int pass) {
	// if (pass == 0) {
	// return overlay;
	// } else {
	// FluidStack fluid = getFluid(stack);
	// PotionProperties p = NBTEffectHelper.getProperties(fluid);
	// if (p != null) {
	// return p.isSplash ? splashBottle : itemIcon;
	// }
	// }
	// return itemIcon;
	// }

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		FluidStack fluid = getFluid(stack);
		if (fluid != null) {
			PotionProperties properties = NBTEffectHelper.getProperties(fluid);
			if (properties.isSplash) {
				list.add(I18n.format("item.potion.splash"));
			}
			if (properties.isBlessed) {
				list.add(I18n.format("item.potion.blessed"));
			}
			if (properties.isCursed) {
				list.add(I18n.format("item.potion.cursed"));
			}
			Effects effects = NBTEffectHelper.getEffects(stack);
			if (effects != null) {
				for (int i = 0; i < effects.size(); i++) {
					Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
					String s1 = I18n.format(potion.getName());

					s1 = s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1));

					list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + (potion.isInstant() ? "" : " (" + Potion.getDurationString(effects.getEffect(i)) + ")"));
				}
			}
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
		// int usesLeft = stack.getItemDamage();
		Effects e = NBTEffectHelper.getEffects(stack);
		if (e == null) {
			return stack;
		}
		List<PotionEffect> effects = e.getEffects();

		ModPotionHelper.addEffectsToEntity(effects, player);
		PotionProperties p = NBTEffectHelper.getProperties(getFluid(stack));
		if (p != null && p.isPersistent) {
			if (!player.getEntityData().hasKey(NBTNames.PERSISTENT_IDS)) {
				player.getEntityData().setIntArray(NBTNames.PERSISTENT_IDS, ModPotionHelper.potionsToIntArray(effects)[0]);
			} else {
				player.getEntityData().setIntArray(NBTNames.PERSISTENT_IDS, ModPotionHelper.mergeIntArrays(player.getEntityData().getIntArray("AI.PersistentIDs"), ModPotionHelper.potionsToIntArray(effects)[0]));
			}
		}
		if (!player.capabilities.isCreativeMode) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.glass_bottle));
		}
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		Effects effects = NBTEffectHelper.getEffects(stack);
		int base = 24;
		return effects != null ? effects.size() * base : base;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		FluidStack fluid = getFluid(stack);
		PotionProperties properties = NBTEffectHelper.getProperties(fluid);
		if (properties != null) {
			if (!NBTEffectHelper.getProperties(getFluid(stack).tag).isSplash) {
				player.setItemInUse(stack, getMaxItemUseDuration(stack));
			} else {
				if (!player.capabilities.isCreativeMode) {
					stack.stackSize--;
				}
				world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				if (!world.isRemote) {
					world.spawnEntityInWorld(new EntitySplashPotion(world, player, NBTEffectHelper.getEffects(stack)));
				}
			}
		}
		return stack;
	}

	@Override
	public int fill(ItemStack stack, FluidStack fluid, boolean doFill) {
		if (fluid != null && fluid.getFluid() != ModFluids.potion) {
			Log.warn("Tried to fill potion bucket with non-potion fluid");
			return 0;
		}
		return super.fill(stack, fluid, doFill);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean currentItem) {
		FluidStack fluid = getFluid(stack);
		if (fluid == null || fluid.amount <= 0) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.inventory.setInventorySlotContents(slot, new ItemStack(Items.glass_bottle, stack.stackSize));
			}
		}
	}

}
